package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private Integer userId;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(1);
        user.setName("John");
        user.setEmail("john@email.com");

        userDto = new UserDto(1, "John", "john@email.com");
    }

    @Test
    void addUserValidUserDtoShouldReturnUserDto() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void updateValidUserShouldReturnUpdatedUserDto() {
        userId = 1;
        UserDto updatedUserDto = new UserDto(userId, "Updated", "updated@email.com");
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUser(userId, updatedUserDto);

        assertNotNull(result);
        assertEquals(updatedUserDto, result);
    }

    @Test
    void updateUserNotFoundThrowsNotFoundException() {
        userId = 1;
        UserDto updatedUserDto = new UserDto(userId, "Updated", "updated@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, updatedUserDto));
    }

    @Test
    void getUserByIdShouldReturnUserDto() {
        userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void getUserByIdWhenUserNotFoundThrowsNotFoundException() {
        userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(userId));
    }

    @Test
    void getAllUsersReturnsListOfUserDto() {
        List<User> users = List.of(user);
        List<UserDto> expected = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(expected, result);
    }

}
