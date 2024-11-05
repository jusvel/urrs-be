package com.example.events.repositories;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EventAttendeesRepository {
    @Insert("INSERT INTO event_attendees (event_id, user_id) VALUES (#{eventId}, #{userId})")
    public void registerForEvent(Long eventId, Long userId);
}
