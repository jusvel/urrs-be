package com.example.events.repositories;

import com.example.events.dto.EventDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AttendeeRepository {
    @Select("SELECT COUNT(*) FROM event_attendees WHERE event_id = #{eventId}")
    int getAttendeeCount(Long eventId);

    @Select("SELECT user_id FROM event_attendees WHERE event_id = ${eventId}")
    List<Long> getAttendeeIds(Long eventId);

    @Insert("INSERT INTO event_attendees (event_id, user_id) VALUES (#{eventId}, #{userId})")
    void registerForEvent(Long eventId, Long userId);

    @Delete("DELETE FROM event_attendees WHERE event_id = #{eventId} AND user_id = #{userId}")
    void unregisterFromEvent(Long eventId, Long userId);

    @Select(" select * FROM events where events.id IN (SELECT event_attendees.event_id FROM event_attendees where user_id = #{userId} and event_id = #{eventId})")
    List<EventDto> isRegisteredToEvent(Long userId, Long eventId);
}
