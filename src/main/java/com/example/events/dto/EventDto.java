package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EventDto {
    private int id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private int userId;
}
