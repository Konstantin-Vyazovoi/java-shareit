package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    UserDto getUser(int id);

    List<UserDto> getUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(int id, UserDto user);

    void deleteUser(int id);

}
