package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
