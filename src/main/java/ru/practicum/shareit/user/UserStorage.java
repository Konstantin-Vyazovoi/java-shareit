package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    UserDto getUser(int id);

    List<UserDto> getUsers();

    UserDto createUser(User user);

    UserDto updateUser(int id, UserDto user);

    void deleteUser(int id);

}
