package com.example.events.services;

import com.example.events.dto.CredentialsDto;
import com.example.events.dto.SignUpDto;
import com.example.events.dto.UserDto;
import com.example.events.exceptions.AppException;
import com.example.events.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto login(CredentialsDto credentialsDto) {
        UserDto userDto = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), userDto.getToken())) {
            return userDto.builder()
                    .id(userDto.getId())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .email(userDto.getEmail())
                    .token(userDto.getToken())
                    .build();
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto){
        Optional<UserDto> optionalUserDto = userRepository.findByLogin(signUpDto.getEmail());

        if(optionalUserDto.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }


        UserDto user = UserDto.builder()
        .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .email(signUpDto.getEmail())
                .token(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())))
                .build();

        userRepository.save(user);

        return user;
    }

    public UserDto findByLogin(String login) {
        UserDto userDto = userRepository.findByLogin(login)
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userDto.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .token(userDto.getToken())
                .build();
    }

    public Long getCurrentUserId () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) authentication.getPrincipal();
        return userDto.getId();
    }
}
