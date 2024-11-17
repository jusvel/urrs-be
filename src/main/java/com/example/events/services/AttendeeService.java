package com.example.events.services;

import com.example.events.dto.EventDto;
import com.example.events.dto.UserResponseDto;
import com.example.events.repositories.AttendeeRepository;
import com.example.events.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public int getAttendeeCount(Long eventId) {
        return attendeeRepository.getAttendeeCount(eventId);
    }

    public List<UserResponseDto> getAttendees(Long eventId) {
        List<Long> attendeeIds = attendeeRepository.getAttendeeIds(eventId);
        List<UserResponseDto> attendees = Collections.emptyList();
        attendeeIds.forEach(userId -> {
            attendees.add(userRepository.getUserById(userId));
        });
        return attendees;
    }

    public void registerForEvent(Long eventId) {
        Long userId = userService.getCurrentUserId();
        attendeeRepository.registerForEvent(eventId, userId);
    }

    public void unregisterFromEvent(Long eventId) {
        Long userId = userService.getCurrentUserId();
        attendeeRepository.unregisterFromEvent(eventId, userId);
    }

    public boolean isRegisteredToEvent(Long eventId) {
        Long userId = userService.getCurrentUserId();
        List<EventDto> eventDtos = attendeeRepository.isRegisteredToEvent(userId, eventId);
        if(eventDtos.size()>0){
            return true;
        } else {
            return false;
        }
    }

}
