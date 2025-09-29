package com.example.itview_spring.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Integer userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        // 초기 이벤트
        try {
            emitter.send(SseEmitter.event()
                .name("connected")
                .data("{}"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    public void sendNotification(Integer userId) {
        System.out.println("sendNotification to userId: " + userId);
        try {
            SseEmitter emitter = emitters.get(userId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                        .name("new-notification")
                        .data("{}"));
                } catch (IOException e) {
                    emitters.remove(userId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving emitter: " + e.getMessage());
            return;
        }
    }
}
