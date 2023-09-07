package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.item.comment.dto.CommentMapper.toCommentList;
import static ru.practicum.shareit.item.dto.ItemMapper.fromItemDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemService(ItemRepository itemStorage, UserRepository userStorage, BookingRepository bookingRepository,
                       CommentRepository commentRepository) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public List<ItemDto> getItems(int userId) {
        log.info("Получение списка предметов пользователя с id: {}", userId);
        return itemDtoList(itemStorage.findAllByOwnerId(userId));
    }

    @Transactional
    public ItemDto getItem(Integer itemId, Integer userId) {
        log.info("Получение предмета с id: {}", itemId);
        Optional<Item> item = itemStorage.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
        log.info("Предмет получен : {}", item);
        List<Comment> commentList = commentRepository.findAllByItemId(item.get().getId());
        ItemDto itemDto = toItemDto(item.get(), toCommentList(commentList));
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
            return toItemDto(itemStorage.save(item), null);
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
        List<Comment> commentList = commentRepository.findAllByItemId(item.getId());
        return toItemDto(itemStorage.save(item), toCommentList(commentList));
    }

    @Transactional
    public List<ItemDto> searchItems(String searchText) {
        log.info("Поиск предмета по строке: {}", searchText);
        if (searchText.isBlank()) return Collections.emptyList();
        return itemDtoList(itemStorage.searchItem(searchText));
    }

    @Transactional
    public CommentDtoResponse createComment(Integer authorId, Integer itemId, CommentDto commentDto) {
        log.info("Создание коментария: {}", commentDto.getText());
        List<Booking> bookings = bookingRepository
            .findAllByBookerIdAndItemIdAndEndDateBeforeAndStatus(authorId,
                itemId,
                LocalDateTime.now(),
                BookingStatus.APPROVED);
        if (bookings.isEmpty()) throw new BadRequestException("Пользователь не может оставить комментарий");
        Item item = validateItem(itemId);
        User author = validateUser(authorId);
        Comment comment = CommentMapper.toComment(commentDto, item, author);
        return CommentMapper.toCommentDtoResponse(commentRepository.save(comment));
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

    private User validateUser(Integer id) {
        if (id == null) throw new ValidateException("Не правильный id");
        Optional<User> user = userStorage.findById(id);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
        return user.get();
    }

    private Item validateItem(Integer itemId) {
        Optional<Item> item = itemStorage.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
        if (!item.get().getAvailable()) throw new BadRequestException("Предмет занят");
        return item.get();
    }

    private List<ItemDto> itemDtoList(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : items) {
            List<Comment> commentList = commentRepository.findAllByItemId(item.getId());
            ItemDto itemDto = toItemDto(item, toCommentList(commentList));
            addBookings(itemDto);
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

}