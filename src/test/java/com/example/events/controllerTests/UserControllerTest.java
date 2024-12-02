package com.example.events.controllerTests;


import com.example.events.controllers.UserController;
import com.example.events.dto.UserResponseDto;
import com.example.events.repositories.AttendeeRepository;
import com.example.events.repositories.UserRepository;
import com.example.events.services.AttendeeService;
import com.example.events.services.UserService;
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
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;



    @Test
    @WithMockUser
    void testGetUsers() throws Exception {

        List<UserResponseDto> users = Arrays.asList(
                new UserResponseDto(1L, "User1", "Doe", "user1@example.com", "USER"),
                new UserResponseDto(2L, "User2", "Smith", "user2@example.com", "USER")
        );

        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'firstName':'User1','lastName':'Doe','email':'user1@example.com','role':'USER'},"
                        + "{'id':2,'firstName':'User2','lastName':'Smith','email':'user2@example.com','role':'USER'}]"));
    }

    @Test
    @WithMockUser
    void testGetCurrentUserId() throws Exception {

        Long expectedUserId = 123L;
        when(userService.getCurrentUserId()).thenReturn(expectedUserId);


        mockMvc.perform(get("/users/current"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUserId.toString()));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetRoles() throws Exception {
        List<String> roles = Arrays.asList("USER", "ATTENDEE", "ORGANISER");
        when(userService.getRoles()).thenReturn(roles);

        mockMvc.perform(get("/users/roles"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"USER\",\"ATTENDEE\",\"ORGANISER\"]"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateRole() throws Exception {
        Long userId = 1L;
        String roleNameJson = "{\"roleName\":\"USER\"}";

        mockMvc.perform(put("/users/" + userId)
                        .contentType("application/json")
                        .content(roleNameJson))
                .andExpect(status().isOk());
        
        verify(userService, times(1)).updateRole("USER", userId);
    }


}