package com.example.events.repositories;

import com.example.events.dto.ReviewDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewRepository {
    @Select("SELECT * FROM event_reviews where event_id = #{eventId}")
    List<ReviewDto> getReviews(Long eventId);

    @Insert("INSERT INTO event_reviews (event_id, user_id, text, rating) values (#{eventId}, #{userId}, #{text}, ${rating})")
    void createReview(Long eventId, Long userId, String text, int rating);
}
