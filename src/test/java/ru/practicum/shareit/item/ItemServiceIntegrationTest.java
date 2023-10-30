package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceIntegrationTest {

    @InjectMocks
    private ItemService itemService;

    private ItemDto itemDto;
    private User user;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "User", "user@email.com");
        itemDto = ItemDto.builder()
            .id(1)
            .name("Item")
            .description("Description")
            .available(true)
            .build();
        itemService = new ItemService(itemRepository, userRepository, bookingRepository, commentRepository);
    }

    @Test
    public void getItemByIdTest() {
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwnerId(1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(item)).thenReturn(item);
        ItemDto response = itemService.createItem(itemDto, 1);
        assertEquals(itemDto.getName(), response.getName());
        assertEquals(itemDto.getDescription(), response.getDescription());
    }

    @Test
    public void getItemThrowNotFoundExceptionTest() {
        assertThrows(NotFoundException.class, () -> itemService.getItem(2, 1));
    }

    @Test
    public void updateItemThrowNotFoundExceptionTest() {
        assertThrows(NotFoundException.class, () -> itemService.updateItem(2, itemDto, 1));
    }

}
