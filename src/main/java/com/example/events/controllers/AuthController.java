package com.example.events.controllers;

import com.example.events.config.UserAuthProvider;
import com.example.events.dto.CredentialsDto;
import com.example.events.dto.SignUpDto;
import com.example.events.dto.UserDto;
import com.example.events.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto){
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthProvider.createToken(userDto.getEmail(), userDto.getRole()));
        return ResponseEntity.ok(userDto);
    };

    @PostMapping("/register")
    public ResponseEntity<UserDto> register (@RequestBody @Valid SignUpDto signUpDto) {
        UserDto createdUser = userService.register(signUpDto);
        createdUser.setToken(userAuthProvider.createToken(createdUser.getEmail(), "USER"));
        return ResponseEntity.created(URI.create("/users" + createdUser.getId())).body(createdUser);
    }
}
