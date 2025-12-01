# import mysql.connector
from geographiclib.geodesic import Geodesic
import random
from datetime import datetime, timedelta
import math
import requests
import argparse
import sys


BACKEND_URL = "http://localhost:8080/api/"

# --- tiny color helper ---
class C:
    RED   = "\033[31m"
    GREEN = "\033[32m"
    YELLOW = "\033[33m"
    CYAN  = "\033[36m"
    RESET = "\033[0m"


def parse_args():
    parser = argparse.ArgumentParser(description="Flight data generator (REST-based)")

    parser.add_argument(
        "--flights", "-n",
        type=int,
        default=1,
        help="Number of flights to generate (default: 1)"
    )

    parser.add_argument(
        "--speed", "-s",
        type=int,
        default=850,
        help="Average cruising speed in km/h (default: 850)"
    )

    parser.add_argument(
        "--start", "-a",
        type=str,
        default="2025-01-01T00:00:00",
        help="Start of generation window (ISO: YYYY-MM-DDTHH:MM:SS)"
    )

    parser.add_argument(
        "--end", "-b",
        type=str,
        default="2025-01-02T00:00:00",
        help="End of generation window (ISO: YYYY-MM-DDTHH:MM:SS)"
    )

    parser.add_argument(
        "--batch", "-B",
        type=int,
        default=10000,
        help="Batch size for bulk upload (default: 10000)"
    )

    parser.add_argument(
        "--dry-run", "-d",
        type=bool,
        default=False,
        help="Only show config, do not call backend"
    )

    args = parser.parse_args()

    # parse dates
    try:
        args.start = datetime.fromisoformat(args.start)
    except ValueError:
        print(f"{C.RED}Invalid --start datetime format. Use YYYY-MM-DDTHH:MM:SS{C.RESET}")
        sys.exit(1)

    try:
        args.end = datetime.fromisoformat(args.end)
    except ValueError:
        print(f"{C.RED}Invalid --end datetime format. Use YYYY-MM-DDTHH:MM:SS{C.RESET}")
        sys.exit(1)

    return args


def validate_args(args):
    errors = []

    if args.flights <= 0:
        errors.append("--flights must be > 0")

    if args.speed <= 0:
        errors.append("--speed must be > 0")

    if args.batch <= 0:
        errors.append("--batch must be > 0")

    if args.start >= args.end:
        errors.append("--start must be earlier than --end")

    if errors:
        print(f"{C.RED}Configuration errors:{C.RESET}")
        for e in errors:
            print(f"  - {e}")
        sys.exit(1)


def print_config(args):
    print(f"{C.CYAN}Flight generator configuration:{C.RESET}")
    print(f"  Flights:     {args.flights}")
    print(f"  Speed:       {args.speed} km/h")
    print(f"  Window:      {args.start}  →  {args.end}")
    print(f"  Batch size:  {args.batch}")
    print(f"  Dry run:     {args.dry_run}")


def haversine_km(lat1, lon1, lat2, lon2):
    R = 6371.0  # km
    phi1 = math.radians(lat1)
    phi2 = math.radians(lat2)
    dphi = math.radians(lat2 - lat1)
    dlambda = math.radians(lon2 - lon1)

    a = math.sin(dphi/2)**2 + math.cos(phi1)*math.cos(phi2)*math.sin(dlambda/2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    return R * c

def lerp(a, b, t: float):
    return a + (b - a) * t

def geodesic_path(lat1, lng1, lat2, lng2, num_points):
    g = Geodesic.WGS84.Inverse(lat1, lng1, lat2, lng2)
    l = Geodesic.WGS84.Line(lat1, lng1, g['azi1'])

    points = []
    for i in range(num_points+1):
        s = g['s12'] * (i / num_points)
        p = l.Position(s)
        points.append((p['lat2'], p['lon2']))

    return points

def random_datetime(start: datetime, end: datetime) -> datetime:
    delta_seconds = int((end - start).total_seconds())
    offset_seconds = random.randint(0, delta_seconds)
    return start + timedelta(seconds=offset_seconds)

def fetch_airports(url):
    print("Sending request...", end="")
    r = requests.get(url)
    print(r.status_code, end=". ")
    print(f"Fetched {len(r.json())} airports")
    return r.json()

def create_flight(url, payload) -> int:
    print("Sending POST request...", end="")
    r = requests.post(url, json=payload)
    print(r.status_code, " id=", r.json()["id"])
    return int(r.json()["id"])

def send_coords(url, positions):
    print(f"start: {positions[0][0]}, end: {positions[-1][0]}")
    print(f"Sending {len(positions)} positions...", end="")
    for i, pos in enumerate(positions):
        payload = {
            "lat": pos[1],
            "lng": pos[2],
            "time": pos[0]             
        }

        try:
            r = requests.post(url, json=payload)
            if r.status_code != 200:
                print(f"[{i}] ERROR {r.status_code}: {r.text}")
            else:
                pass
        except requests.exceptions.RequestException as e:
            print(f"[{i}] REQUEST FAILED: {e}")

def chunks(iterable, size):
    for i in range(0, len(iterable), size):
        yield iterable[i:i+size]

def send_coords_bulk(url, positions, batch_size=5000):
    print(f"Sending {len(positions)} positions in batches of {batch_size}...")

    for batch_idx, batch in enumerate(chunks(positions, batch_size), start=1):
        payload = [
            {"time": ts, "lat": lat, "lng": lng}
            for (ts, lat, lng) in batch
        ]

        r = requests.post(url, json=payload, timeout=10)

        if r.ok:
            print(f"[batch {batch_idx}] OK ({len(batch)} positions)")
        else:
            print(f"[batch {batch_idx}] ERROR {r.status_code}: {r.text[:200]}")
            break      

# BUG: Can generate flights ending after END_DATE if the flight itself is too long
def generate_n_flights(n: int, airports, START_DATE, END_DATE, BATCH_SIZE):

    print(f"Generating {n} flight(s) from {START_DATE} to {END_DATE}...")
    
    if len(airports) < 2:
        print("Need at least 2 airports")
        return

    for _ in range(n):
        start, end = random.sample(airports, 2)
        dist_km = haversine_km(start["lat"], start["lng"], end["lat"], end["lng"])
        hours = dist_km / AVG_SPEED_KMH
        duration = timedelta(hours=hours)


        total_seconds = int(duration.total_seconds())

        start_date = random_datetime(START_DATE, END_DATE - duration)

        # Generate positions
        coords = geodesic_path(start["lat"], start["lng"], end["lat"], end["lng"], total_seconds)
        positions = []
        for i, (lat, lng) in enumerate(coords):
            ts = (start_date + timedelta(seconds=i)).strftime("%Y-%m-%dT%H:%M:%S")
            positions.append((ts, lat, lng))
        
        # for i in range(total_seconds):
        #     t = i / total_seconds
        #     lat = lerp(start["lat"], end["lat"], t)
        #     lng = lerp(start["lng"], end["lng"], t)
        #     ts = (start_date + timedelta(seconds=i)).strftime("%Y-%m-%dT%H:%M:%S")
        #     positions.append((ts, lat, lng))


        print(f"Generated {len(positions)} positions")

        # Create the Flight Record in the Backend
        payload = {
            "startLatitude": start["lat"],
            "startLongitude": start["lng"],
            "startLocationName": start["name"],
            "endLatitude": end["lat"],
            "endLongitude": end["lng"],
            "endLocationName": end["name"] 
        }

        id = create_flight(BACKEND_URL + "flights", payload)

        # Send the Flight positions to the Backend
        send_coords_bulk(f"http://localhost:8080/api/flights/{id}/positions/bulk", positions, BATCH_SIZE)



if __name__ == "__main__":
    args = parse_args()
    validate_args(args)
    print_config(args)

    # Here you map args → your existing globals, or pass directly:
    N_FLIGHTS = args.flights
    AVG_SPEED_KMH = args.speed
    START_DATE = args.start
    END_DATE = args.end
    BATCH_SIZE = args.batch

    if args.dry_run:
        print(f"{C.YELLOW}Dry run enabled, not generating any flights.{C.RESET}")
        sys.exit(0)

    # Assuming you already have:
    #   airports = fetch_airports()
    #   generate_n_flights(N_FLIGHTS, airports, START_DATE, END_DATE, BATCH_SIZE, ...)
    #
    airports = fetch_airports(BACKEND_URL + "airports")
    print(f"{C.GREEN}Generating {N_FLIGHTS} flight(s) from {START_DATE} to {END_DATE}...{C.RESET}")
    generate_n_flights(N_FLIGHTS, airports, START_DATE, END_DATE, BATCH_SIZE)


        
