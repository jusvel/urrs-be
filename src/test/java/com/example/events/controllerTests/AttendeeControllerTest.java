package com.example.events.controllerTests;

import com.example.events.controllers.AttendeeController;
import com.example.events.dto.UserResponseDto;
import com.example.events.services.AttendeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AttendeeController.class)
public class AttendeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendeeService attendeeService;

    @Test
    @WithMockUser
    void testGetAttendeeCount() throws Exception {
        Long eventId = 1L;
        when(attendeeService.getAttendeeCount(eventId)).thenReturn(10);

        mockMvc.perform(get("/attendees/{eventId}/count", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testGetAttendees() throws Exception {
        Long eventId = 1L;
        List<UserResponseDto> attendees = Arrays.asList(
                new UserResponseDto(1L, "User1", "Doe", "user1@example.com", "USER"),
                new UserResponseDto(2L, "User2", "Smith", "user2@example.com", "USER")
        );

        when(attendeeService.getAttendees(eventId)).thenReturn(attendees);

        mockMvc.perform(get("/attendees/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'firstName':'User1','lastName':'Doe','email':'user1@example.com','role':'USER'},"
                        + "{'id':2,'firstName':'User2','lastName':'Smith','email':'user2@example.com','role':'USER'}]"));
    }

    @Test
    @WithMockUser(username = "2", roles = "USER")
    void testUnregisterFromEvent() throws Exception {
        Long eventId = 1L;

        when(attendeeService.isRegisteredToEvent(eventId)).thenReturn(true);

        mockMvc.perform(delete("/attendees/register/{eventId}", eventId))
                .andExpect(status().isOk());

        verify(attendeeService).unregisterFromEvent(eventId);
    }

    @Test
    @WithMockUser(username = "2", roles = "USER")
    void testRegisterFromEvent() throws Exception {
        Long eventId = 1L;

        when(attendeeService.isRegisteredToEvent(eventId)).thenReturn(false);

        mockMvc.perform(post("/attendees/register/{eventId}", eventId))
                .andExpect(status().isOk());

        verify(attendeeService).registerForEvent(eventId);
    }

    @Test
    @WithMockUser
    void testIsRegisteredToEvent() throws Exception {
        Long eventId = 1L;
        when(attendeeService.isRegisteredToEvent(eventId)).thenReturn(true);

        mockMvc.perform(get("/attendees/registered/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}