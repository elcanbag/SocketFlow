
INSERT INTO users (username, password, email, verification_code, verified) VALUES
    ('testuser', 'testpassword', 'elcanbaa@proton.me', 888576, TRUE);

-- DEVICES
INSERT INTO devices (access_token, name, owner_id) VALUES
                                                       ('81ac1f43-56ac-45d1-a647-ac6d63332428', 'device1', 1),
                                                       ('f2b19312-9f04-4f21-a2d5-f4cd44a1fdad', 'greenhouse_sensor', 1),
                                                       ('82d9e0ab-1df6-4df9-b16b-97db7a6c2c99', 'weather_station', 1);

-- FIELD
INSERT INTO field (device_id, name, type, unit) VALUES
-- device1
(1, 'Temperature', 'temperature', '°C'),
(1, 'Humidity',    'humidity',    '%'),

-- greenhouse_sensor
(2, 'Soil Moisture', 'moisture', '%'),
(2, 'Light Level',   'light',    'lux'),

-- weather_station
(3, 'Wind Speed', 'wind', 'km/h'),
(3, 'Rainfall', 'rain', 'mm'),
(3, 'Temperature', 'temperature', '°C');

-- FIELD_RECORD
INSERT INTO field_record (timestamp, data_value, field_id) VALUES
-- device1 datas
('2025-04-28T12:00:00', 24.5, 1),
('2025-04-28T12:00:00', 60.2, 2),
('2025-04-28T13:00:00', 25.1, 1),
('2025-04-28T13:00:00', 58.9, 2),

-- greenhouse_sensor datas
('2025-04-28T12:30:00', 78.3, 3),
('2025-04-28T12:30:00', 450.0, 4),
('2025-04-28T13:30:00', 76.8, 3),
('2025-04-28T13:30:00', 420.5, 4),

-- weather_station datas
('2025-04-28T14:00:00', 12.5, 5),
('2025-04-28T14:00:00', 3.2, 6),
('2025-04-28T14:00:00', 22.0, 7),
('2025-04-28T15:00:00', 11.0, 5),
('2025-04-28T15:00:00', 4.1, 6),
('2025-04-28T15:00:00', 21.3, 7);
