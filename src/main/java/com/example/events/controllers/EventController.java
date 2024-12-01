package com.example.events.controllers;

import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.model.EventType;
import com.example.events.services.EventService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEvents() {
        return eventService.getEvents();
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    public void createEvent(@RequestBody EventRequestDto eventRequestDto) {
        eventService.createEvent(eventRequestDto);
    }

    @GetMapping("/registered")
    public List<EventDto> getRegisteredEvents() {
        return eventService.getRegisteredEvents();
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{eventId}")
    public void updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDto eventRequestDto) {
        eventService.updateEvent(eventId, eventRequestDto);
    }

    @GetMapping("/types")
    public List<EventType> getEventTypes() { return eventService.getEventTypes(); }

    @PreAuthorize("hasRole('ORGANIZER')")
    @GetMapping("/{eventId}/export")
    public ResponseEntity<InputStreamResource> exportEventDetails(@PathVariable long eventId) {
        ByteArrayInputStream excelFile = eventService.generateEventExcel(eventId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=event_" + eventId + ".xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(excelFile));
    }

}
