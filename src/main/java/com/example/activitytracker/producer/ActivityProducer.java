package com.example.activitytracker.producer;

import com.example.activitytracker.dto.ActivityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityProducer {

    private final KafkaTemplate<String, ActivityEvent> kafkaTemplate;

    private static final String TOPIC = "user-activity-events";

    public void sendEvent(ActivityEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}