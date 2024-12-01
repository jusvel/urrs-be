package com.example.events.repositories;

import com.example.events.dto.UserDto;
import com.example.events.dto.UserResponseDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {
    @Select("select u.id, u.first_name, u.last_name, u.email, u.password, r.role_name from users u INNER JOIN roles r ON u.role = r.id WHERE email = #{login}")
    Optional<UserDto> findByLogin(String login);

    @Insert("INSERT INTO users (first_name, last_name, email, password, role) VALUES (#{firstName}, #{lastName}, #{email}, #{token}, 0)")
    void save(UserDto userDto);

    @Select("SELECT u.id, u.first_name, u.last_name, u.email, r.role_name FROM users u INNER JOIN roles r ON u.role = r.id WHERE u.id = #{userId}")
    UserResponseDto getUserById(Long userId);

    @Select("SELECT u.id, u.first_name, u.last_name, u.email, r.role_name from users u INNER JOIN roles r ON u.role = r.id WHERE u.id != #{userId}")
    List<UserResponseDto> getUsers(Long currentUserId);

    @Select("SELECT role_name FROM roles")
    List<String> getRoles();

    @Update("UPDATE users SET role = #{roleId} WHERE id = #{userId}")
    void updateUserRole(int roleId, Long userId);

    @Select("SELECT id from roles where role_name = #{roleName}")
    int getRoleIdByName(String roleName);
}
