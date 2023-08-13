package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserGroup;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        log.info("Запрошен пользователь с id: {} ", id);
        UserDto user = userService.getUser(id);
        log.info("Запрос на пользователя с id {} выполнен: {} ", id, user);
        return user;
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        log.info("Запрошен список Всех пользователей");
        List<UserDto> users = userService.getUsers();
        log.info("Запрос на предоставление списка всех пользователей выполнен");
        return users;
    }

    @PostMapping()
    public UserDto createUser(@Validated(CreateUserGroup.class) @RequestBody UserDto user) {
        log.info("Запрошено создание пользователя: {} ", user);
        UserDto createdUser = userService.createUser(user);
        log.info("Запрос на создание пользователя выполнен, пользователь создан: {} ", createdUser);
        return createdUser;
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody UserDto user, @PathVariable int id) {
        log.info("Запрошено обновление пользователя: {} ", user);
        UserDto updatedUser = userService.updateUser(id, user);
        log.info("Запрос выполнен, пользователь обновлен: {} ", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Запрошено на удаление пользователя с id {} ", id);
        userService.deleteUser(id);
        log.info("Запрос на удаление пользователя id {} выполнен", id);
    }
}
