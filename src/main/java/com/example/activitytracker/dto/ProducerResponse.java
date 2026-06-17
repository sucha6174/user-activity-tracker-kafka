package com.example.activitytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProducerResponse {

    private String status;
    private String eventId;
}