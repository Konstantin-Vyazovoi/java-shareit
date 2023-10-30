package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private BookingDto bookingDto;
    private UserDto userDto;
    private UserDto userDto2;
    private ItemDto itemDto;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void beforeEach() {
        userDto = new UserDto(1, "User", "user@email.com");
        userDto2 = new UserDto(2, "User2", "user2@email.com");
        itemDto = new ItemDto(1,
            "ItemName",
            "Description",
            true,
            null,
            null,
            new ArrayList<>(),
            null);
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);
        bookingDto = new BookingDto(start, end, 1);
        userDto = userService.createUser(userDto);
        userDto2 = userService.createUser(userDto2);
        itemDto = itemService.createItem(itemDto, 1);
    }

    @Test
    public void createBookingTest() {
        BookingResponseDto responseDto = bookingService.createBooking(bookingDto, 2);
        Assertions.assertNotNull(responseDto);
        assertEquals(responseDto.getItem().getId(), bookingDto.getItemId());
        assertEquals(responseDto.getStart(), bookingDto.getStart());
        assertEquals(responseDto.getEnd(), bookingDto.getEnd());
        assertEquals(responseDto.getStatus(), BookingStatus.WAITING);
        assertEquals(responseDto.getBooker().getId(), userDto2.getId());
    }

    @Test
    public void updateBookingTest() {
        BookingResponseDto responseDto = bookingService.createBooking(bookingDto, 2);
        Assertions.assertNotNull(responseDto);
        responseDto = bookingService.updateBookingStatus(1, 1, true);
        assertEquals(responseDto.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void getByIdBookingTest() {
        BookingResponseDto responseDto = bookingService.createBooking(bookingDto, 2);
        Assertions.assertNotNull(responseDto);
        responseDto = bookingService.getById(1, 1);
        assertEquals(responseDto.getItem().getId(), bookingDto.getItemId());
        assertEquals(responseDto.getStatus(), BookingStatus.WAITING);
        assertEquals(responseDto.getBooker().getId(), userDto2.getId());
    }

    @Test
    public void getUserBookingsTest() {
        BookingResponseDto responseDto = bookingService.createBooking(bookingDto, 2);
        Assertions.assertNotNull(responseDto);
        List<BookingResponseDto> responseDtoList = bookingService.getBookings(2, "ALL", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
        responseDtoList = bookingService.getBookings(2, "FUTURE", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
        responseDtoList = bookingService.getBookings(2, "REJECTED", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookings(2, "CURRENT", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookings(2, "PAST", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookings(2, "WAITING", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
    }

    @Test
    public void getUserBookingsThrowBadRequestExceptionTest() {
        bookingService.createBooking(bookingDto, 2);
        assertThrows(BadRequestException.class, () -> bookingService.getBookings(2, "ALL", -1, 2));
    }

    @Test
    public void getOwnerBookingsTest() {
        BookingResponseDto responseDto = bookingService.createBooking(bookingDto, 2);
        Assertions.assertNotNull(responseDto);
        List<BookingResponseDto> responseDtoList = bookingService.getBookingsByOwner(1, "ALL", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
        responseDtoList = bookingService.getBookingsByOwner(1, "FUTURE", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
        responseDtoList = bookingService.getBookingsByOwner(1, "REJECTED", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookingsByOwner(1, "CURRENT", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookingsByOwner(1, "PAST", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 0);
        responseDtoList = bookingService.getBookingsByOwner(1, "WAITING", 1, 2);
        Assertions.assertNotNull(responseDtoList);
        assertEquals(responseDtoList.size(), 1);
    }

}
