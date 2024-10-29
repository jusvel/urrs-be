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
}
