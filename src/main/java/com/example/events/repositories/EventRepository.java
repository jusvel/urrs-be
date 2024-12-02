package com.example.events.repositories;

import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.model.EventType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface EventRepository {
    @Select("SELECT * FROM events")
    List<EventDto> getEvents();
    @Select("SELECT * FROM events WHERE " +
            "(#{title} IS NULL OR title LIKE CONCAT('%', #{title}, '%')) AND " +
            "(#{eventDate} IS NULL OR DATE(event_date) = DATE(#{eventDate})) AND " +
            "(#{eventType} IS NULL OR event_type = #{eventType})")
    List<EventDto> getEventsByFilter(@Param("title") String title,
                                     @Param("eventDate") LocalDate eventDate,
                                     @Param("eventType") EventType eventType);

    @Select("SELECT * FROM events WHERE id = #{eventId}")
    EventDto getEventById(Long eventId);

    @Insert("INSERT INTO events (title, description, location, event_date, created_on, created_by, event_type) values (#{title}, #{description}, #{location}, #{eventDate}, #{createdOn}, #{userId}, #{eventType})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createEvent(EventDto eventDto);

    @Select("SELECT * FROM events where events.id IN (SELECT event_attendees.event_id FROM event_attendees where user_id = #{userId})")
    List<EventDto> getRegisteredEvents(Long userId);

    @Delete("DELETE FROM events WHERE id = #{eventId}")
    void deleteEvent(Long eventId);

    @Update("UPDATE events SET title = #{title}, description = #{description}, location = #{location}, event_type = #{eventType}, event_date = #{eventDate} WHERE id = #{id}")
    void updateEvent(Long id, String title, String description, String location, EventType eventType, Timestamp eventDate);
}
