package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private BookingRepository bookingRepository;

    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    public void beforeEach() {
        itemDto = new ItemDto(1,
            "Стул",
            "Просто стул",
            true,
            null,
            null,
            new ArrayList<>(),
            null);
        userDto = new UserDto(2, "User", "user@email.com");
    }

    @Test
    public void createItemTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());

        ItemDto actualItemDto = itemService.createItem(itemDto, user.getId());
        assertNotNull(actualItemDto.getId());

        assertEquals(itemDto.getName(), actualItemDto.getName());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
    }

    @Test
    public void updateItemTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());
        itemService.createItem(itemDto, user.getId());

        itemDto.setAvailable(false);
        ItemDto actualItemDto = itemService.updateItem(1, itemDto, 1);
        assertEquals(itemDto.getName(), actualItemDto.getName());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
    }

    @Test
    public void getByIdItemTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());
        itemService.createItem(itemDto, user.getId());
        ItemDto actualItemDto = itemService.getItem(1, 1);
        assertEquals(itemDto.getName(), actualItemDto.getName());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
    }

    @Test
    public void getOwnerItemsTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());
        itemService.createItem(itemDto, user.getId());
        List<ItemDto> items = itemService.getItems(1);
        assertEquals(items.size(), 1);
        assertEquals(itemDto.getName(), items.get(0).getName());
        assertEquals(itemDto.getDescription(), items.get(0).getDescription());
        assertEquals(itemDto.getAvailable(), items.get(0).getAvailable());
    }

    @Test
    public void searchItemTest() {
        UserDto user = userService.createUser(userDto);
        assertNotNull(user.getId());
        itemService.createItem(itemDto, user.getId());
        List<ItemDto> items = itemService.searchItems("Ст");
        assertEquals(items.size(), 1);
        assertEquals(itemDto.getName(), items.get(0).getName());
        assertEquals(itemDto.getDescription(), items.get(0).getDescription());
        assertEquals(itemDto.getAvailable(), items.get(0).getAvailable());
    }

    @Test
    public void addCommentToItemTest() {
        userService.createUser(userDto);
        UserDto booker = new UserDto(2, "User2", "someemail@em.com");
        userService.createUser(booker);
        itemService.createItem(itemDto, 1);
        Booking booking = new Booking(1,
            LocalDateTime.now().minusDays(2),
            LocalDateTime.now().minusDays(1),
            ItemMapper.fromItemDto(itemDto),
            UserMapper.fromUserDto(booker),
            BookingStatus.APPROVED);
        bookingRepository.save(booking);
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Some comment");
        CommentDtoResponse actualComment = itemService.createComment(2, 1, commentDto);
        assertNotNull(actualComment);
        assertEquals(actualComment.getAuthorName(), "User2");
        assertEquals(actualComment.getText(), "Some comment");
    }

}
