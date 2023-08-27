package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;

public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


}
