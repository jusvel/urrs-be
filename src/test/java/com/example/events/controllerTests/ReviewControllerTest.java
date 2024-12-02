package com.example.events.controllerTests;

import com.example.events.controllers.ReviewController;
import com.example.events.dto.ReviewDto;
import com.example.events.dto.ReviewRequestDto;
import com.example.events.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    void testCreateReview() throws Exception {
        // Prepare the request payload
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("Great event!", 5);
        Long eventId = 1L;

        // Mock the service call (no return value for void method)
        doNothing().when(reviewService).createReview(eventId, reviewRequestDto);

        // Perform the POST request and expect status 200
        mockMvc.perform(post("/reviews/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewRequestDto)))
                .andExpect(status().isOk());

        // Verify the service call was made
        verify(reviewService).createReview(eventId, reviewRequestDto);
    }

    @Test
    void testGetReviews() throws Exception {
        // Prepare mock data
        ReviewDto review1 = new ReviewDto(1, 1, 1, 5, "Great event!");
        ReviewDto review2 = new ReviewDto(2, 1, 2, 4, "Good but crowded.");
        int eventId = 1;
        List<ReviewDto> reviews = Arrays.asList(review1, review2);

        // Mock the service call
        when(reviewService.getReviews((long) eventId)).thenReturn(reviews);

        // Perform the GET request and assert the response
        mockMvc.perform(get("/reviews/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Great event!"))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].text").value("Good but crowded."))
                .andExpect(jsonPath("$[1].rating").value(4));

        // Verify the service call was made
        verify(reviewService).getReviews((long) eventId);
    }
}
