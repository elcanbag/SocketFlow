
INSERT INTO cubesats (id, name, access_token) VALUES (1, 'TestCube', 'token123');


INSERT INTO users (id, username, password, cubesat_id) VALUES (1, 'testuser', '123456', 1);


INSERT INTO dates (
    id, cubesat_id, record_date, temperature, humidity, x, y, z,
    lat, longg, internal_temp, pressure, alt, sat, received_at
) VALUES (
             1, 1, '2025-05-01T12:00:00', '24.5', '60.2', '0.1', '0.2', '0.3',
             '40.4093', '49.8671', '33.3', '1012', '78.5', '5', '2025-05-01T12:00:00'
         );
