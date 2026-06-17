package com.example.activitytracker.consumer;

import com.example.activitytracker.dto.ActivityEvent;
import com.example.activitytracker.store.ActivityStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityConsumer {

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

        System.out.println("Consumed Event: " + event);
    }
}