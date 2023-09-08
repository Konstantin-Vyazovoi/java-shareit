package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.shareit.booking.dto.BookingMapper.*;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingDto bookingDto, Integer bookerId) {
        validateDate(bookingDto);
        User booker = validateUser(bookerId);
        Item item = validateItem(bookingDto);
        if (item == null)
            throw new NotFoundException(
                String.format("Предмет с id %s не найден", bookingDto.getItemId()));
        if (!item.getAvailable())
            throw new BadRequestException(
                String.format("Предмет с id %s занят", bookingDto.getItemId()));
        if (item.getOwnerId() == bookerId) throw new NotFoundException("Нельзя взять в аренду свой предмет");
        Booking booking = toBooking(bookingDto);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        log.info("Создание бронирования {}", booking);
        return toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto updateBookingStatus(Integer bookingId, Integer userId, Boolean approved) {
        validateUser(userId);
        Booking booking = validateBooking(bookingId);
        Optional<Item> item = itemRepository.findById(booking.getItem().getId());
        if (item.isEmpty())
            throw new NotFoundException(String.format("Предмет с id %s не найден", booking.getItem().getId()));
        boolean isOwnerRequest = item.get().getOwnerId() == userId;
        if (userId.equals(booking.getBooker().getId()))
            throw new NotFoundException("Нет доступа к изменению статуса бронирования");
        if (!isOwnerRequest) throw new BadRequestException("Нет доступа к изменению статуса бронирования");
        if (booking.getStatus() != BookingStatus.WAITING) throw new BadRequestException("Не правльный статус");
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return toBookingResponseDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingResponseDto getById(Integer userId, Integer bookingId) {
        validateUser(userId);
        Booking booking = validateBooking(bookingId);
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
            booking.getItem().getOwnerId() != userId) {
            throw new NotFoundException(String
                .format("Нет доступа к бронированию с id %s, у пользователя с id %s", bookingId, userId));
        }
        return toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookings(Integer bookerId, String status) {
        validateUser(bookerId);
        LocalDateTime dateNow = LocalDateTime.now();
        switch (status.toUpperCase()) {
            case "ALL":
                List<Booking> bookingList = bookingRepository.findAllByBookerIdOrderByStartDateDesc(bookerId);
                return toListBookingResp(bookingList);
            case "CURRENT":
                return toListBookingResp(bookingRepository
                    .findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        bookerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()));
            case "FUTURE":
                return toListBookingResp(bookingRepository
                    .findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(
                        bookerId,
                        LocalDateTime.now()));
            case "PAST":
                return toListBookingResp(bookingRepository
                    .findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(
                        bookerId,
                        LocalDateTime.now()));
            case "WAITING":
            case "REJECTED":
                return toListBookingResp(bookingRepository
                    .findAllByBookerIdAndStatusOrderByStartDateDesc(bookerId,
                        BookingStatus.valueOf(status)));
            default:
                throw new BadRequestException(String.format("Unknown state: %s", status));
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Integer userId, String status) {
        validateUser(userId);
        LocalDateTime dateNow = LocalDateTime.now();
        switch (status.toUpperCase()) {
            case "ALL":
                return toListBookingResp(bookingRepository
                    .findAllByItemOwnerIdOrderByStartDateDesc(userId));
            case "CURRENT":
                return toListBookingResp(bookingRepository
                    .findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        userId,
                        LocalDateTime.now(),
                        LocalDateTime.now()));
            case "FUTURE":
                return toListBookingResp(bookingRepository
                    .findAllByItemOwnerIdAndStartDateAfterOrderByStartDateDesc(
                        userId,
                        LocalDateTime.now()));
            case "PAST":
                return toListBookingResp(bookingRepository
                    .findAllByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(
                        userId,
                        LocalDateTime.now()));
            case "WAITING":
            case "REJECTED":
                return toListBookingResp(bookingRepository
                    .findAllByItemOwnerIdAndStatusOrderByStartDateDesc(userId,
                        BookingStatus.valueOf(status)));
            default:
                throw new BadRequestException(String.format("Unknown state: %s", status));
        }
    }

    private void validateDate(BookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd()) || bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestException("Не правильные даты!");
        }
    }

    private User validateUser(Integer id) {
        if (id == null) throw new ValidateException(String.format("Не правильный id %s", id));
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
        return user.get();
    }

    private Item validateItem(BookingDto bookingDto) {
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) throw new NotFoundException(String.format("Предмет с id %s не найден", bookingDto.getItemId()));
        if (!item.get().getAvailable()) throw new BadRequestException("Предмет занят");
        return item.get();
    }

    private Booking validateBooking(Integer bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) throw new NotFoundException(String.format("Бронирование с id %s не найдено", bookingId));
        return booking.get();
    }

}