package com.example.events.controllers;

import com.example.events.dto.ReviewDto;
import com.example.events.dto.ReviewRequestDto;
import com.example.events.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{eventId}")
    public void createReview(@RequestBody ReviewRequestDto reviewRequestDto, @PathVariable Long eventId) {
        reviewService.createReview(eventId, reviewRequestDto);
    }

    @GetMapping("/{eventId}")
    public List<ReviewDto> getReviews(@PathVariable Long eventId) {
        return reviewService.getReviews(eventId);
    }
}
