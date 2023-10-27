package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto(1, "user", "user@user.com");
    }

    @Test
    void shouldCreateTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());

        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    void shouldUpdateTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());

        UserDto updateUser = new UserDto(1, "update", "update@user.com");
        Integer userId = 1;

        UserDto actualUser = userService.updateUser(userId, updateUser);

        assertEquals(updateUser.getName(), actualUser.getName());
        assertEquals(updateUser.getEmail(), actualUser.getEmail());
        assertEquals(userId, actualUser.getId());
    }

    @Test
    void shouldGetByIdTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());

        UserDto actualUser = userService.getUser(user.getId());
        assertEquals(user, actualUser);
    }

    @Test
    void shouldGetAllTest() {
        UserDto userDto2 = new UserDto(2, "user2", "user2@user.com");

        UserDto user = userService.createUser(userDto);
        UserDto user2 = userService.createUser(userDto2);

        List<UserDto> expectedUsers = List.of(user, user2);
        List<UserDto> actualUsers = userService.getUsers();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void shouldDeleteByIdTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());

        userService.deleteUser(user.getId());

        assertEquals(new ArrayList<>(), userService.getUsers());
    }

    @Test
    void shouldGetUserIfExistsTest() {
        userService.createUser(userDto);
        UserDto user = userService.getUser(1);
        assertNotNull(user.getId());
    }
}