package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (bookingDto.getStart() != null) booking.setStartDate(bookingDto.getStart());
        if (bookingDto.getEnd() != null) booking.setEndDate(bookingDto.getEnd());
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
            .id(booking.getId())
            .start(booking.getStartDate())
            .end(booking.getEndDate())
            .status(booking.getStatus())
            .booker(booking.getBooker())
            .item(booking.getItem())
            .build();
    }

    public static List<BookingResponseDto> toListBookingResp(List<Booking> bookingList) {
        return bookingList.stream()
            .map(booking -> toBookingResponseDto(booking))
            .collect(Collectors.toList());
    }

    public static BookingItemDto toBookingItemDto(Booking booking) {
        return new BookingItemDto(
            booking.getId(),
            booking.getBooker().getId()
        );
    }
}
