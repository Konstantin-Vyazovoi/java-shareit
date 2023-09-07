package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStartDateDesc(Integer bookerId);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
        Integer bookerId, LocalDateTime dateTime, LocalDateTime dateTime1);

    List<Booking> findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(Integer bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(Integer bookerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdOrderByStartDateDesc(Integer bookerId);

    List<Booking> findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
        Integer bookerId, LocalDateTime dateTime, LocalDateTime dateTime1);

    List<Booking> findAllByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(Integer bookerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartDateAfterOrderByStartDateDesc(Integer bookerId, LocalDateTime dateTime);

    Optional<Booking> findFirstBookingByItemIdAndStartDateBeforeAndStatusNotOrderByStartDateDesc(
        Integer itemId, LocalDateTime now, BookingStatus bookingStatus);

    Optional<Booking> findFirstBookingByItemIdAndStartDateAfterAndStatusNotOrderByStartDate(
        Integer itemId, LocalDateTime now, BookingStatus bookingStatus);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(Integer userId, BookingStatus valueOf);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDateDesc(Integer userId, BookingStatus valueOf);

    List<Booking> findAllByBookerIdAndItemIdAndEndDateBeforeAndStatus(Integer userId,
                                                                      Integer itemId,
                                                                      LocalDateTime dateTime,
                                                                      BookingStatus status);
}
