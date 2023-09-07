package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.item.dto.ItemMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemService(ItemRepository itemStorage, UserRepository userStorage, BookingRepository bookingRepository) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public List<ItemDto> getItems(int userId) {
        log.info("Получение списка предметов пользователя с id: {}", userId);
        List<ItemDto> itemDtoList = itemDtoList(itemStorage.findAllByOwnerId(userId));
        for (ItemDto itemDto: itemDtoList) {
            addBookings(itemDto);
        }
        return itemDtoList;
    }

    @Transactional
    public ItemDto getItem(Integer itemId, Integer userId) {
        log.info("Получение предмета с id: {}", itemId);
        Optional<Item> item = itemStorage.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
        log.info("Предмет получен : {}", item);
        ItemDto itemDto = toItemDto(item.get());
        if (item.get().getOwnerId() == userId) addBookings(itemDto);
        return itemDto;
    }

    @Transactional
    public ItemDto createItem(ItemDto itemDto, int userId) {
        log.info("Создание предмета с id пользователя: {}", userId);
        if (userId > 0 && userStorage.findById(userId).isPresent()) {
            log.info("Создание предмета : {}, ", itemDto);
            Item item = fromItemDto(itemDto);
            item.setOwnerId(userId);
            return toItemDto(itemStorage.save(item));
        } else throw new NotFoundException("Пользователь не найден");
    }

    @Transactional
    public ItemDto updateItem(Integer id, ItemDto itemDto, Integer owner) {
        log.info("Обновление предмета с id пользователя {}, id предмета {}", owner, id);
        Optional<Item> optionalItem = itemStorage.findById(id);
        if (optionalItem.isEmpty()) throw new NotFoundException("Предмет не найден");
        Item item = optionalItem.get();
        if (optionalItem.get().getOwnerId() != owner) throw new NotFoundException("Пользователь не найден");
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        return toItemDto(itemStorage.save(item));
    }

    @Transactional
    public List<ItemDto> searchItems(String searchText) {
        log.info("Поиск предмета по строке: {}", searchText);
        if (searchText.isBlank()) return Collections.emptyList();
        return itemDtoList(itemStorage.
            searchItem
                (searchText));
    }

    private void addBookings(ItemDto itemDto) {
        Optional<Booking> lastBooking = bookingRepository
            .findFirstBookingByItemIdAndStartDateBeforeAndStatusNotOrderByStartDateDesc(itemDto.getId(),
                LocalDateTime.now(), BookingStatus.REJECTED);
        lastBooking.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toBookingItemDto(lastBooking.get())));
        Optional<Booking> nextBooking = bookingRepository
            .findFirstBookingByItemIdAndStartDateAfterAndStatusNotOrderByStartDate(itemDto.getId(),
                LocalDateTime.now(), BookingStatus.REJECTED);
        nextBooking.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toBookingItemDto(nextBooking.get())));
    }
}