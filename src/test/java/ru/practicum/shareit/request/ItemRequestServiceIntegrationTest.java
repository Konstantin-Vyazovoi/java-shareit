package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceIntegrationTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestDto itemRequestDto;
    private User user;

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "User", "user@email.com");
        itemRequestDto = new ItemRequestDto("description");
        userRepository.save(user);
        itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto));
    }

    @Test
    public void getRequestsThrowBadRequestExceptionTest() {
        assertThrows(BadRequestException.class, () -> itemRequestService.getAllRequests(1, -2, 2));
    }

    @Test
    public void getRequestsThrowValidateExceptionTest() {
        assertThrows(ValidateException.class, () -> itemRequestService.getAllRequests(null, 0, 2));
    }

}
