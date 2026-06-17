package com.example.activitytracker.dto;

import lombok.Data;

@Data
public class ActivityEvent {
    private String eventId;
    private String userId;
    private String action;
    private String resourceId;
    private long timestamp;
}