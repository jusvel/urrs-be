package com.example.events.repositories;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EventAttendeesRepository {
    @Insert("INSERT INTO event_attendees (event_id, user_id) VALUES (#{eventId}, #{userId})")
    public void registerForEvent(Long eventId, Long userId);

    @Delete("DELETE FROM event_attendees WHERE event_id = #{eventId} AND user_id = #{userId}")
    public void unregisterFromEvent(Long eventId, Long userId);
}
