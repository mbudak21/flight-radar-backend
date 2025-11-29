CREATE DATABASE IF NOT EXISTS `mysql-db`;
USE `mysql-db`;

# Tables
CREATE TABLE IF NOT EXISTS flight_record (
    ID INT AUTO_INCREMENT PRIMARY KEY,

    start_latitude DOUBLE NOT NULL,
    start_longitude DOUBLE NOT NULL,
    start_location_name VARCHAR(255),

    end_latitude DOUBLE,
    end_longitude DOUBLE,
    end_location_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS flight_position (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    flight_ID INT NOT NULL,
    time DATETIME NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    FOREIGN KEY (flight_ID) REFERENCES flight_record(ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    UNIQUE (flight_ID, time) -- Ensure data integrity
);

CREATE TABLE IF NOT EXISTS airports (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    lat double not NULL,
    lng double not NULL
);

# Dummy Data
INSERT INTO flight_record (
    start_latitude, start_longitude, start_location_name,
    end_latitude, end_longitude, end_location_name
)
VALUES
    -- Flight 1: Istanbul -> Berlin
    (
        41.2753, 28.7519, 'Istanbul Airport (IST)',
        52.3667, 13.5033, 'Berlin Brandenburg (BER)'
    ),
    -- Flight 2: Ankara -> London
    (
        40.1281, 32.9951, 'Ankara Esenboğa (ESB)',
        51.4700, -0.4543, 'London Heathrow (LHR)'
    );

INSERT INTO flight_position (flight_ID, time, latitude, longitude)
VALUES
    (1, '2025-11-29 00:35:00', 40.1281, 32.9951),
    (1, '2025-11-29 00:35:01', 40.1286, 32.9951),
    (1, '2025-11-29 00:35:02', 40.1289, 32.9951),

    (2, '2025-11-29 00:35:00', 32.9951, 32.9951);


# # Utility
# SELECT * FROM airports;
# DROP TABLE airports;
#
# SELECT * FROM flight_record;
# SELECT * FROM flight_position;
#
# DROP TABLE flight_record;
# DROP TABLE flight_position;
