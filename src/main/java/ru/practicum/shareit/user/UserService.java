package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    @Qualifier("InMemoryUserStorage")
    private UserStorage userStorage;

    public UserDto getUser(int id) {
        validateUserEmail(userStorage.getUser(id), id);
        return userStorage.getUser(id);
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }

    public UserDto createUser(User user) {
        if (!userStorage.getUsers().isEmpty()) {
            validateUserEmail(UserMapper.toUserDto(user), user.getId());
        }
        return userStorage.createUser(user);
    }

    public UserDto updateUser(int id, UserDto user) {
        validateUserEmail(user, id);
        return userStorage.updateUser(id, user);
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    private void validateUserEmail(UserDto user, Integer id) {
        List<UserDto> userList;
        userList = getUsers()
            .stream()
            .filter(u -> u.getEmail().equals(user.getEmail()))
            .collect(Collectors.toList());
        if (!userList.isEmpty() && !userList.contains(userStorage.getUser(id))) {
            throw new ValidateException("Пользователь с такой почтой уже есть");
        }
    }
}
