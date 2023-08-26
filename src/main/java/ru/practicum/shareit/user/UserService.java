package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userStorage;

    @Autowired
    public UserService(UserRepository userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto getUser(int id) {
        Optional<User> optionalUser = userStorage.findById(id);
        if (optionalUser.isEmpty()) throw new NotFoundException("Пользователь не найден!");
        return toUserDto(optionalUser.get());
    }

    @Transactional
    public List<UserDto> getUsers() {
        return userDtoList(userStorage.findAll());
    }

    @Transactional
    public UserDto createUser(@Valid UserDto userDto) {
        User user = fromUserDto(userDto);
        return toUserDto(userStorage.save(user));
    }

    @Transactional
    public UserDto updateUser(int id, UserDto userDto) {
        Optional<User> optionalUser = userStorage.findById(id);
        if (optionalUser.isEmpty()) throw new NotFoundException("Пользователь не найден!");
        User user = optionalUser.get();
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getName() != null) user.setName(userDto.getName());
        User user1 = userStorage.save(user);
        UserDto userDto1 = toUserDto(user1);
        return userDto1;
    }

    @Transactional
    public void deleteUser(int id) {
        userStorage.deleteById(id);
    }

}