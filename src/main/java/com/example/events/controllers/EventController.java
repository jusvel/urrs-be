package com.example.events.controllers;

import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEvents() { return eventService.getEvents(); }

    @PostMapping
    public void createEvent(@RequestBody EventRequestDto eventRequestDto) { eventService.createEvent(eventRequestDto); }

    @GetMapping("/registered")
    public List<EventDto> getRegisteredEvents() {return eventService.getRegisteredEvents(); }


}
