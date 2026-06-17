package com.example.activitytracker.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.activitytracker.dto.ActivityEvent;
import com.example.activitytracker.dto.ActivityStats;

@Component
public class ActivityStore {

    private final Map<String, List<ActivityEvent>> activityMap = new ConcurrentHashMap<>();

    public void save(ActivityEvent event) {
        activityMap
                .computeIfAbsent(event.getUserId(), k -> new ArrayList<>())
                .add(event);
    }

    public List<ActivityEvent> getUserActivities(String userId) {
        return activityMap.getOrDefault(userId, Collections.emptyList());
    }

    public ActivityStats getStats() {

        ActivityStats stats = new ActivityStats();

        long totalEvents = activityMap.values()
                .stream()
                .mapToLong(List::size)
                .sum();

        Map<String, Long> eventsByAction = new HashMap<>();

        activityMap.values().forEach(events -> {
            events.forEach(event -> {
                eventsByAction.merge(
                        event.getAction(),
                        1L,
                        Long::sum
                );
            });
        });

        stats.setTotalEvents(totalEvents);
        stats.setEventsByAction(eventsByAction);

        return stats;
    }
}