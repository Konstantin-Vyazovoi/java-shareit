package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto getUser(int id) {
        validateUserEmail(userStorage.getUser(id), id);
        UserDto userDto = userStorage.getUser(id);
        if (userDto == null) throw new NotFoundException("Пользователь не найден!");
        return userDto;
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }

    public UserDto createUser(@Valid UserDto user) {
        if (!userStorage.getUsers().isEmpty()) {
            validateUserEmail(user, 0);
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

    private void validateUserEmail(UserDto userDto, int id) {
        List<UserDto> userList;
        userList = getUsers()
            .stream()
            .filter(u -> u.getEmail().equals(userDto.getEmail()))
            .collect(Collectors.toList());
        UserDto user = userStorage.getUser(id);
        if (!userList.isEmpty() && !userList.contains(user)) {
            throw new ValidateException("Пользователь с такой почтой уже есть");
        }
    }
}