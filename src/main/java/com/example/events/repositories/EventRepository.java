package com.example.events.repositories;

import com.example.events.dto.EventDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EventRepository {
    @Select("SELECT * FROM events")
    List<EventDto> getEvents();

    @Insert("INSERT INTO events (title, description, location, event_date, created_on, created_by) values (#{title}, #{description}, #{location}, #{eventDate}, #{createdOn}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void createEvent(EventDto eventDto);

    @Select("SELECT * FROM events where events.id IN (SELECT event_attendees.event_id FROM event_attendees where user_id = #{userId})")
    List<EventDto> getRegisteredEvents(Long userId);


}
