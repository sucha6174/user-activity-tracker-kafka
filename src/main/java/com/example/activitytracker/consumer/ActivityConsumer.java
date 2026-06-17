package com.example.activitytracker.consumer;

import com.example.activitytracker.dto.ActivityEvent;
import com.example.activitytracker.store.ActivityStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class ActivityConsumer {
    private static final Logger log =
        LoggerFactory.getLogger(ActivityConsumer.class);
    private final ActivityStore activityStore;

    @KafkaListener(
            topics = "user-activity-events",
            groupId = "activity-group"
    )
    public void consume(ActivityEvent event) {

        if (event.getUserId() == null) {
            throw new RuntimeException("UserId cannot be null");
        }

        activityStore.save(event);

        log.info("Consumed Event: {}", event);
    }
}