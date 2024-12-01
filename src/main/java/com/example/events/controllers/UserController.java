package com.example.events.controllers;

import com.example.events.dto.UserResponseDto;
import com.example.events.services.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/current")
    public Long getCurrentUserId() {
        return userService.getCurrentUserId();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public List<String> getRoles() {
        return userService.getRoles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public void updateRole(@PathVariable Long userId, @RequestBody String roleNameJson) {
        JsonObject jsonObject = JsonParser.parseString(roleNameJson).getAsJsonObject();
        String roleName = jsonObject.get("roleName").getAsString();
        userService.updateRole(roleName, userId);
    }
}
