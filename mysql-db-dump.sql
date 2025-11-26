CREATE DATABASE IF NOT EXISTS `mysql-db`;
USE `mysql-db`;


CREATE TABLE IF NOT EXISTS FlightRecord (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    StartDate DATETIME NOT NULL,
    StartLocation VARCHAR(255),
    EndLocation VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS FlightPosition (
    PID INT NOT NULL,
    DateTime DATETIME NOT NULL,
    Latitude DOUBLE NOT NULL,
    Longitude DOUBLE NOT NULL,
    FOREIGN KEY (PID) REFERENCES FlightRecord(ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    PRIMARY KEY (PID, DateTime)
);

-- INSERT INTO FlightRecord (StartDate, StartLocation, EndLocation)
-- VALUES
--     ('2025-11-01 09:00:00', 'Istanbul', 'Berlin'),
--     ('2025-11-02 14:30:00', 'Ankara', 'London');
--
-- INSERT INTO FlightPosition (PID, DateTime, Latitude, Longitude)
-- VALUES
--     (1, '2025-11-01 09:05:00', 41.0082, 28.9784),
--     (1, '2025-11-01 09:20:00', 42.2000, 29.5000);
--
-- INSERT INTO FlightPosition (PID, DateTime, Latitude, Longitude)
-- VALUES
--     (2, '2025-11-02 14:35:00', 39.9334, 32.8597),
--     (2, '2025-11-02 14:50:00', 40.5000, 30.0000);