package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        return bookingDto;
    }

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (bookingDto.getStart() != null) booking.setStart(bookingDto.getStart());
        if (bookingDto.getEnd() != null) booking.setEnd(bookingDto.getEnd());
        if (bookingDto.getStatus() != null) booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
            .id(booking.getId())
            .start(booking.getStart())
            .end(booking.getEnd())
            .status(booking.getStatus())
            .booker(UserMapper.toUserDto(booking.getBooker()))
            .item(ItemMapper.toItemDto(booking.getItem()))
            .build();
    }

    public static List<BookingResponseDto> toListBookingResp(List<Booking> bookingList) {
        return bookingList.stream()
            .map(BookingMapper::toBookingResponseDto)
            .collect(Collectors.toList());
    }
}
