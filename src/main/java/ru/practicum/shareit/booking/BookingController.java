package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.interfaces.CreateGroup;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final String headerUserId = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(headerUserId) Integer userId,
                                            @RequestBody @Validated(CreateGroup.class)BookingDto bookingDto) {
        log.info("Получен запрос на создание бронирования: {}", bookingDto);
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(@RequestHeader(headerUserId) Integer userId,
                                            @PathVariable("bookingId") Integer bookingId, Boolean approved) {
        log.info("Получен запрос обновления от пользователя с id: {}, на бронирование: {}, со статусом: {}",
            userId, bookingId, approved);
        return bookingService.updateBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(headerUserId) Integer userId,
                                            @PathVariable("bookingId") Integer bookingId) {
        log.info("Получен запрос получения от пользователя с id: {}, на бронирование: {}", userId, bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookerBookingList(@RequestHeader(headerUserId) Integer userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос получения от пользователя с id: {}, и статусом: {}", userId, state);
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookingList(@RequestHeader(headerUserId) Integer userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен запрос получения от владельца с id: {}, и статусом: {}", userId, state);
        return bookingService.getBookingsByOwner(userId, state);
    }

}
