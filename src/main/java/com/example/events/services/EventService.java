package com.example.events.services;

import com.example.events.dto.EventDto;
import com.example.events.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<EventDto> getEvents() { return eventRepository.getEvents(); }
}
