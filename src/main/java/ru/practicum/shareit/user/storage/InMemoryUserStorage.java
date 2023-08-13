package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerated = 0;

    @Override
    public UserDto getUser(int id) {
        if (users.containsKey(id)) {
            return UserMapper.toUserDto(users.get(id));
        } else return null;
    }

    @Override
    public List<UserDto> getUsers() {
        Collection<User> userList = users.values();
        return UserMapper.userDtoList(userList);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        user.setId(generateId());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        User user = users.get(id);
        if (user != null) {
            String name = userDto.getName();
            String email = userDto.getEmail();
            if (name != null && !name.isBlank()) {
                user.setName(userDto.getName());
            }
            if (email != null && !email.isBlank()) {
                user.setEmail(userDto.getEmail());
            }
            return UserMapper.toUserDto(user);
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
