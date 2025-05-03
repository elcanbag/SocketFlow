package com.example.cubesat.websocket;

import com.example.cubesat.repository.DeviceRepository;
import com.example.cubesat.repository.FieldRecordRepository;
import com.example.cubesat.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final DeviceRepository deviceRepository;
    private final FieldRepository fieldRepository;
    private final FieldRecordRepository fieldRecordRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new DeviceWebSocketHandler(deviceRepository, fieldRepository, fieldRecordRepository), "/ws/device")
                .setAllowedOrigins("*");
    }
}
