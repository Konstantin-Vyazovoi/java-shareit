package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerId(Integer bookerId, Sort sort);
    List<Booking> findAllByBooker_IdAndEndAfterAndStartBefore(Integer bookerId, LocalDate endDate,
                                                              LocalDate startDate, Sort sort);
    List<Booking> findAllByBooker_IdAndStartAfter(Integer bookerId, LocalDate startDate, Sort sort);
    List<Booking> findAllByBooker_IdAndEndBefore(Integer bookerId, LocalDate endDate, Sort sort);
    List<Booking> findAllByBooker_IdAndStatus(Integer bookerId, BookingStatus status, Sort sort);

    List<Booking> findAllByItem_OwnerId(Integer ownerId, Sort sort);
    List<Booking> findAllByItem_OwnerIdAndEndAfterAndStartBefore(Integer ownerId, LocalDate endDate,
                                                                 LocalDate startDate, Sort sort);
    List<Booking> findAllByItem_OwnerIdAndStartAfter(Integer ownerId, LocalDate startDate, Sort sort);
    List<Booking> findAllByItem_OwnerIdAndEndBefore(Integer ownerId, LocalDate endDate, Sort sort);
    List<Booking> findAllByItem_OwnerIdAndStatus(Integer ownerId, BookingStatus status, Sort sort);

}
