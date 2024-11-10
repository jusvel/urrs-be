package com.example.events.repositories;

import com.example.events.dto.EventDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EventRepository {
    @Select("SELECT * FROM events")
    List<EventDto> getEvents();

    @Select("SELECT * FROM events where events.id IN (SELECT event_attendees.event_id FROM event_attendees where user_id = #{userId})")
    List<EventDto> getRegisteredEvents(Long userId);

    @Select(" select * FROM events where events.id IN (SELECT event_attendees.event_id FROM event_attendees where user_id = #{userId} and event_id = #{eventId})")
    List<EventDto> isRegisteredToEvent(Long userId, Long eventId);
}
