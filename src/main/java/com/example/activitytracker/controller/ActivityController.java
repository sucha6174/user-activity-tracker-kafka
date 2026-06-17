package com.example.activitytracker.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.activitytracker.dto.ActivityEvent;
import com.example.activitytracker.dto.ActivityStats;
import com.example.activitytracker.dto.ProducerResponse;
import com.example.activitytracker.producer.ActivityProducer;
import com.example.activitytracker.store.ActivityStore;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityProducer activityProducer;
    private final ActivityStore activityStore;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProducerResponse publishEvent(@RequestBody ActivityEvent event) {

       String eventId = UUID.randomUUID().toString();

event.setEventId(eventId);

activityProducer.sendEvent(event);
        return new ProducerResponse(
                "ACCEPTED",
                eventId
        );
    }

    @GetMapping("/{userId}")
    public List<ActivityEvent> getActivities(@PathVariable String userId) {
        return activityStore.getUserActivities(userId);
    }
    @GetMapping("/stats")
public ActivityStats getStats() {
    return activityStore.getStats();
}
}