package com.example.events.repositories;

import com.example.events.dto.UserDto;
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

    @Insert("INSERT INTO users (first_name, last_name, email, password) VALUES (#{firstName}, #{lastName}, #{email}, #{token})")
    void save(UserDto userDto);
}
