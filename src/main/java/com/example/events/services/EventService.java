package com.example.events.services;

import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final UserService userService;
    private final EventRepository eventRepository;

    public List<EventDto> getEvents() { return eventRepository.getEvents(); }

    public void deleteEvent(Long eventId) { eventRepository.deleteEvent(eventId); }

    public void updateEvent(Long eventId,EventRequestDto eventRequestDto) {
        Timestamp eventDate = Timestamp.valueOf(eventRequestDto.getEventDate());
        eventRepository.updateEvent(eventId, eventRequestDto.getTitle(), eventRequestDto.getDescription(), eventRequestDto.getLocation(), eventDate);
    }

    public void createEvent(EventRequestDto eventRequestDto) {
        Timestamp createdOn = Timestamp.from(Instant.now());
        Timestamp eventDate = Timestamp.valueOf(eventRequestDto.getEventDate());
        int userId = userService.getCurrentUserId().intValue();
        EventDto eventDto = EventDto.builder()
                .title(eventRequestDto.getTitle())
                .description(eventRequestDto.getDescription())
                .location(eventRequestDto.getLocation())
                .eventDate(eventDate)
                .createdOn(createdOn)
                .userId(userId)
                .build();
        eventRepository.createEvent(eventDto);
    }


    public List<EventDto> getRegisteredEvents() {
        Long userId = userService.getCurrentUserId();
        return eventRepository.getRegisteredEvents(userId);
    }


}
