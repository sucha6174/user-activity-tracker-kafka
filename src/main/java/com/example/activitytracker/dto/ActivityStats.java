package com.example.activitytracker.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ActivityStats {

    private long totalEvents;
    private Map<String, Long> eventsByAction;
}