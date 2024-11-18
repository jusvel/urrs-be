package com.example.events.repositories;

import com.example.events.dto.UserDto;
import com.example.events.dto.UserResponseDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {
    @Select("SELECT * FROM users WHERE email = #{login}")
    Optional<UserDto> findByLogin(String login);

    @Insert("INSERT INTO users (first_name, last_name, email, password, role) VALUES (#{firstName}, #{lastName}, #{email}, #{token}, 0)")
    void save(UserDto userDto);

    @Select("SELECT id, first_name, last_name, email, role FROM users WHERE id = #{userId}")
    UserResponseDto getUserById(Long userId);
}
