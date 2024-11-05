package com.example.events.controllers;

import com.example.events.dto.EventDto;
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

    @PostMapping("/{eventId}")
    public void registerForEvent(@PathVariable Long eventId) {
        eventService.registerForEvent(eventId);
    }
}
