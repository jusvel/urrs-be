package com.example.events.controllerTests;

import com.example.events.controllers.EventController;
import com.example.events.dto.EventDto;
import com.example.events.dto.EventRequestDto;
import com.example.events.model.EventType;
import com.example.events.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private EventService eventService;

    private EventDto event1;
    private EventDto event2;
    private EventRequestDto eventRequestDto;
    private EventType eventType;

    @BeforeEach
    void setUp() {
        eventType = EventType.CONFERENCE;

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        event1 = new EventDto(1, "Event 1", "Description 1", "Location 1", currentTimestamp, currentTimestamp, 1, eventType);
        event2 = new EventDto(2, "Event 2", "Description 2", "Location 2", currentTimestamp, currentTimestamp, 2, eventType);

        eventRequestDto = new EventRequestDto("Event 3", "Description 3", "Location 3", "2024-01-01", eventType);
    }
    @Test
    void testGetEventsByFilter() throws Exception {

        List<EventDto> registeredEvents = Arrays.asList(event1);
        EventRequestDto eventRequestDto1 = eventRequestDto;

        when(eventService.getEventsByFilter(eventRequestDto1)).thenReturn(registeredEvents);

        mockMvc.perform(post("/events/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventRequestDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Event 1"));
    }

    @Test
    void testGetEvents() throws Exception {
        List<EventDto> events = Arrays.asList(event1, event2);
        when(eventService.getEvents()).thenReturn(events);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    void testCreateEvent() throws Exception {

        mockMvc.perform(post("/events")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventRequestDto)))
                .andExpect(status().isOk());

        verify(eventService, times(1)).createEvent(eventRequestDto);
    }

    @Test
    void testGetRegisteredEvents() throws Exception {
        List<EventDto> registeredEvents = Arrays.asList(event1);
        when(eventService.getRegisteredEvents()).thenReturn(registeredEvents);

        mockMvc.perform(get("/events/registered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Event 1"));
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    void testDeleteEvent() throws Exception {
        Long eventId = 1L;

        mockMvc.perform(delete("/events/{eventId}", eventId))
                .andExpect(status().isOk());

        verify(eventService, times(1)).deleteEvent(eventId);
    }

    @Test
    @WithMockUser(roles = "ORGANIZER")
    void testUpdateEvent() throws Exception {
        Long eventId = 1L;

        mockMvc.perform(put("/events/{eventId}", eventId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventRequestDto)))
                .andExpect(status().isOk());

        verify(eventService, times(1)).updateEvent(eventId, eventRequestDto);
    }

    @Test
    void testGetEventTypes() throws Exception {
        List<EventType> eventTypes = Arrays.asList(EventType.CONFERENCE, EventType.WORKSHOP);
        when(eventService.getEventTypes()).thenReturn(eventTypes);

        mockMvc.perform(get("/events/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("CONFERENCE"))
                .andExpect(jsonPath("$[1]").value("WORKSHOP"));
    }
}