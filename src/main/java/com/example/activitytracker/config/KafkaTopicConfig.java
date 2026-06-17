package com.example.activitytracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userActivityEventsTopic() {
        return new NewTopic("user-activity-events", 3, (short) 1);
    }

    @Bean
    public NewTopic userActivityEventsDltTopic() {
        return new NewTopic("user-activity-events.DLT", 1, (short) 1);
    }
}