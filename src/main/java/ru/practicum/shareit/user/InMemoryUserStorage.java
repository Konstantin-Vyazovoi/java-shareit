package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerated = 0;

    @Override
    public UserDto getUser(int id) {
        if (users.containsKey(id)) {
            return UserMapper.toUserDto(users.get(id));
        } else throw new NotFoundException("Пользователь не найден!");
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> userList = new ArrayList(users.values());
        return UserMapper.userDtoList(userList);
    }

    @Override
    public UserDto createUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int id, UserDto user) {
        if (users.containsKey(id)) {
            User storageUser = users.get(id);
            if (user.getName() != null) {
                storageUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                storageUser.setEmail(user.getEmail());
            }
            return UserMapper.toUserDto(storageUser);
        }
        throw new NotFoundException("Пользователь не найден!");
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    private int generateId() {
        return ++idGenerated;
    }
}
