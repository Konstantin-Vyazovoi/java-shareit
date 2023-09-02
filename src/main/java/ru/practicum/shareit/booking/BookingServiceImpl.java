package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.shareit.booking.dto.BookingMapper.*;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Sort sort = Sort.by("start_date");

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
        if (item == null) throw new NotFoundException("Предмет не найден");
        if (!item.getAvailable()) throw new BadRequestException("Предмет занят");
        Booking booking = toBooking(bookingDto);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(booker);
        booking.setItem(item);
        log.info("Создание бронирования {}", booking);
        return toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto updateBookingStatus(int bookingId, int userId, String approved) {
        try {
            validateUser(userId);
            Booking booking = validateBooking(bookingId);
            Optional<Item> item = itemRepository.findById(booking.getItem().getId());
            if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
            boolean isOwnerRequest = item.get().getOwnerId() == userId;
            if (!isOwnerRequest)
                throw new ValidateException("Нет доступа к изменению статуса бронирования");
            if (booking.getStatus() != BookingStatus.WAITING) throw new ValidateException("Не правльный статус");
            if (approved.equals("true")) {
                booking.setStatus(BookingStatus.APPROVED);
            } else if (approved.equals("false")) {
                booking.setStatus(BookingStatus.CANCELED);
            } else throw new BadRequestException("Не корректный статус");
            return toBookingResponseDto(bookingRepository.save(booking));
        } catch (NotFoundException e) {
            throw new NotFoundException("Предмет не найден");
        }
    }

    @Transactional
    @Override
    public BookingResponseDto getById(Integer userId, Integer bookingId) {
        validateUser(userId);
        Booking booking = validateBooking(bookingId);
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
        booking.getItem().getOwnerId() != userId) {
            throw new ValidateException("Нет доступа к бронированию");
        }
        return toBookingResponseDto(booking);
    }

    @Transactional
    @Override
    public List<BookingResponseDto> getBookings(Integer bookerId, String status) {
        validateUser(bookerId);
        LocalDate dateNow = LocalDate.now();
        switch (status.toUpperCase()) {
            case "ALL":
                List<Booking> bookingList = bookingRepository.findByBookerId(bookerId, sort);
                log.info("Получен список бронированния: {}", bookingList);
                return toListBookingResp(bookingList);
            case "CURRENT":
                return toListBookingResp(bookingRepository.findAllByBooker_IdAndEndAfterAndStartBefore(bookerId,
                    dateNow,
                    dateNow,
                    sort));
            case "FUTURE":
                return toListBookingResp(bookingRepository.findAllByBooker_IdAndStartAfter(bookerId, dateNow, sort));
            case "PAST":
                return toListBookingResp(bookingRepository.findAllByBooker_IdAndEndBefore(bookerId, dateNow, sort));
            case "WAITING":
                return toListBookingResp(bookingRepository.findAllByBooker_IdAndStatus(bookerId, BookingStatus.WAITING, sort));
            case "REJECTED":
                return toListBookingResp(bookingRepository.findAllByBooker_IdAndStatus(bookerId, BookingStatus.REJECTED, sort));
            default:
                throw new BadRequestException("Неправильно передан статус");
        }
    }

    @Transactional
    @Override
    public List<BookingResponseDto> getBookingsByOwner(Integer ownerId, String status) {
        validateUser(ownerId);
        LocalDate dateNow = LocalDate.now();
        switch (status.toUpperCase()) {
            case "ALL":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerId(ownerId, sort));
            case "CURRENT":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerIdAndEndAfterAndStartBefore(ownerId,
                    dateNow,
                    dateNow,
                    sort));
            case "FUTURE":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerIdAndStartAfter(ownerId, dateNow, sort));
            case "PAST":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerIdAndEndBefore(ownerId, dateNow, sort));
            case "WAITING":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerIdAndStatus(ownerId, BookingStatus.WAITING, sort));
            case "REJECTED":
                return toListBookingResp(bookingRepository.findAllByItem_OwnerIdAndStatus(ownerId, BookingStatus.REJECTED, sort));
            default:
                throw new BadRequestException("Неправильно передан статус");
        }
    }

    private void validateDate(BookingDto bookingDto) {
        if (bookingDto.getStart().equals(bookingDto.getEnd()) || bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestException("Не правильные даты!");
        }
    }

    private User validateUser(Integer id) {
        if (id == null) throw new ValidateException("Не правильный id");
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
        return user.get();
    }

    private Item validateItem(BookingDto bookingDto) {
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
        if (!item.get().getAvailable()) throw new BadRequestException("Предмет занят");
        return item.get();
    }

    private Booking validateBooking(Integer bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking == null) throw new NotFoundException("Бронирование не найдено");
        return booking;
    }

}
