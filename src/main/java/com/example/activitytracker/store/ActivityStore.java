package com.example.activitytracker.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

import com.example.activitytracker.dto.ActivityEvent;
import com.example.activitytracker.dto.ActivityStats;

@Component
public class ActivityStore {

    private final Map<String, List<ActivityEvent>> activityMap = new ConcurrentHashMap<>();
    private final AtomicLong totalEvents = new AtomicLong(0);

    private final Map<String, AtomicLong> actionCounts = new ConcurrentHashMap<>(); 
    public void save(ActivityEvent event) {
        activityMap
                .computeIfAbsent(
                        event.getUserId(),
                        k -> Collections.synchronizedList(new ArrayList<>())
                )
                .add(event);
                totalEvents.incrementAndGet();

actionCounts
        .computeIfAbsent(event.getAction(), k -> new AtomicLong(0))
        .incrementAndGet();
    }

    public List<ActivityEvent> getUserActivities(String userId) {
        return activityMap.getOrDefault(userId, Collections.emptyList());
    }

    public ActivityStats getStats() {

    ActivityStats stats = new ActivityStats();

    Map<String, Long> eventsByAction = new HashMap<>();

    actionCounts.forEach((action, count) ->
            eventsByAction.put(action, count.get())
    );

    stats.setTotalEvents(totalEvents.get());
    stats.setEventsByAction(eventsByAction);

    return stats;
}
}