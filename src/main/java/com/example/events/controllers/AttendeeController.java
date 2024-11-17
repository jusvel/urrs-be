package com.example.events.controllers;

import com.example.events.dto.UserResponseDto;
import com.example.events.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendees")
public class AttendeeController {
    private final AttendeeService attendeeService;

    @GetMapping("/{eventId}/count")
    public int getAttendeeCount(@PathVariable Long eventId) {
        return attendeeService.getAttendeeCount(eventId);
    }

    @GetMapping("/{eventId}")
    public List<UserResponseDto> getAttendees(@PathVariable Long eventId) {
        return attendeeService.getAttendees(eventId);
    }

    @PostMapping("/register/{eventId}")
    public void registerForEvent(@PathVariable Long eventId) {
        attendeeService.registerForEvent(eventId);
    }

    @DeleteMapping("/register/{eventId}")
    public void unregisterFromEvent(@PathVariable Long eventId) {
        attendeeService.unregisterFromEvent(eventId);
    }

    @GetMapping("/registered/{eventId}")
    public boolean isRegisteredToEvent(@PathVariable Long eventId) { return attendeeService.isRegisteredToEvent(eventId); }
}
