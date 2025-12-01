host = "localhost"
user = "user"
password = "password"
database = "mysql-db"

import csv

airports = []

with open("world-airports.csv", newline='', encoding="utf-8") as f:
    reader = csv.DictReader(f)
    for row in reader:
        iata = row["iata_code"]
        icao = row["icao_code"]

        code = iata if iata else icao
        
        name = f"{row['municipality']} ({code})"
        lat = float(row["latitude_deg"])
        lon = float(row["longitude_deg"])
        type = row["type"]
        if type == "large_airport":
            airports.append((name, lat, lon))

import mysql.connector

conn = mysql.connector.connect(
    host=host,
    user=user,
    password=password,
    database=database
)

cursor = conn.cursor()

insert_sql = """
    INSERT INTO airports (name, lat, lng)
    VALUES (%s, %s, %s)
"""

# airports = list from CSV step above
cursor.executemany(insert_sql, airports)
conn.commit()

cursor.close()
conn.close()
