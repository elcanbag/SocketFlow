INSERT INTO users (username, password) VALUES ('testuser', 'testpassword');
INSERT INTO devices (access_token, name, owner_id) VALUES ('81ac1f43-56ac-45d1-a647-ac6d63332428', 'device1', 1);
INSERT INTO fields (device_id, name, type, unit) VALUES (1, 'Temperature', 'temperature', '°C');
INSERT INTO fields (device_id, name, type, unit) VALUES (1, 'Humidity', 'humidity', '%');
INSERT INTO field_records (recorded_at, data_value, field_id) VALUES ('2025-04-28T12:00:00', '24.5', 1);
