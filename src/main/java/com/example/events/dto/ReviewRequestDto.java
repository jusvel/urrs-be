package com.example.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private String text;
    private int rating;
}
