package com.example.events.dto;

import com.example.events.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@Builder
public class EventRequestDto {
    private String title;
    private String description;
    private String location;
    private String eventDate;
    private EventType eventType;
}
