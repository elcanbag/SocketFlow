package com.example.cubesat.websocket;

import com.example.cubesat.model.Device;
import com.example.cubesat.model.Field;
import com.example.cubesat.model.FieldRecord;
import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.FieldRepository;
import com.example.cubesat.repository.FieldRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DeviceWebSocketHandler extends TextWebSocketHandler {
    private final DeviceRepository deviceRepository;
    private final FieldRepository fieldRepository;
    private final FieldRecordRepository fieldRecordRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        Map<String, String> parsed = parsePayload(payload);

        String token = parsed.get("token");
        String dataString = parsed.get("data");

        Device device = deviceRepository.findByAccessToken(token)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        Map<String, String> sensorData = parseDataString(dataString);

        for (Map.Entry<String, String> entry : sensorData.entrySet()) {
            String type = entry.getKey();
            String value = entry.getValue();

            Field field = fieldRepository.findByDeviceAndType(device, type)
                    .orElseThrow(() -> new RuntimeException("Field not found for type: " + type));

            FieldRecord record = new FieldRecord();
            record.setField(field);
            record.setTimestamp(LocalDateTime.now());
            record.setValue(value);

            fieldRecordRepository.save(record);
        }

        session.sendMessage(new TextMessage("Data received and saved."));
    }

    private Map<String, String> parsePayload(String payload) {
        Map<String, String> map = new HashMap<>();
        String[] parts = payload.split(";");
        for (String part : parts) {
            int firstColonIndex = part.indexOf(':');
            if (firstColonIndex != -1) {
                String key = part.substring(0, firstColonIndex).trim();
                String value = part.substring(firstColonIndex + 1).trim();
                map.put(key, value);
            }
        }
        return map;
    }


    private Map<String, String> parseDataString(String data) {
        Map<String, String> map = new HashMap<>();
        String[] fields = data.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }
}
