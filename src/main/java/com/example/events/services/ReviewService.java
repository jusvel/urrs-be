package com.example.events.services;

import com.example.events.dto.EventRequestDto;
import com.example.events.dto.ReviewDto;
import com.example.events.dto.ReviewRequestDto;
import com.example.events.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public List<ReviewDto> getReviews(Long eventId){
        return reviewRepository.getReviews(eventId);
    }

    public void createReview(Long eventId, ReviewRequestDto reviewRequestDto) {
        Long userId = userService.getCurrentUserId();
        reviewRepository.createReview(eventId, userId, reviewRequestDto.getText(), reviewRequestDto.getRating());
    }
}
