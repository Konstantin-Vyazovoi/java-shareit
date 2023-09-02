package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingDto bookingDto, Integer bookerId);
    BookingResponseDto updateBookingStatus(int bookingId, int ownerId, String approved);
    BookingResponseDto getById(Integer userId, Integer bookingId);
    List<BookingResponseDto> getBookings(Integer bookerId, String approved);
    List<BookingResponseDto> getBookingsByOwner(Integer ownerId, String approved);
}
