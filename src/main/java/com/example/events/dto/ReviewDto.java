package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReviewDto {
    private int id;
    private int eventId;
    private int userId;
    private int rating;
    private String text;
}
