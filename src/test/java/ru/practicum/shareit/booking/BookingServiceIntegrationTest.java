package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceIntegrationTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    private ItemDto itemDto;
    private User user;
    private BookingDto bookingDto;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "User", "user@email.com");
        itemDto = ItemDto.builder()
            .id(1)
            .name("Item")
            .description("Description")
            .available(true)
            .build();
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);
        bookingDto = new BookingDto(start, end, 1);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
    }


    @Test
    public void createBookingThrowNotFoundExceptionTest() {
        bookingDto.setItemId(2);
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(bookingDto, 2));
    }

    @Test
    public void createBookingThrowBadRequestExceptionTest() {
        itemDto.setAvailable(false);
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setOwnerId(1);
        when(userRepository.findById(2)).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(1)).thenReturn(Optional.ofNullable(item));
        assertThrows(BadRequestException.class, () -> bookingService.createBooking(bookingDto, 2));
    }

}
