package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;
    private CommentDto commentDto;
    private ItemDto itemDto;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void beforeEach() {
        itemDto = new ItemDto(1,
            "ItemName",
            "Description",
            true,
            lastBooking,
            nextBooking,
            new ArrayList<>(),
            null);
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);
        lastBooking = new BookingItemDto(1, 1);
        nextBooking = new BookingItemDto(1, 1);
    }

    @SneakyThrows
    @Test
    public void createItemTest() {
        when(itemService.createItem(itemDto, 1)).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("ItemName"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.available").value(true));
    }

    @SneakyThrows
    @Test
    public void updateItemTest() {
        itemDto.setAvailable(false);
        when(itemService.updateItem(1, itemDto, 1)).thenReturn(itemDto);
        mockMvc.perform(patch("/items/{itemId}", 1)
                .content(objectMapper.writeValueAsString(itemDto))
                .header("X-Sharer-User-Id", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("ItemName"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.available").value(false));
    }

    @SneakyThrows
    @Test
    public void getItemByIdTest() {
        when(itemService.getItem(1, 1)).thenReturn(itemDto);
        mockMvc.perform(get("/items/{itemId}", 1)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("ItemName"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.available").value(true));
    }

    @SneakyThrows
    @Test
    public void getItemByOwnerIdTest() {
        List<ItemDto> itemDtoList = List.of(itemDto);
        when(itemService.getItems(1)).thenReturn(itemDtoList);
        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("ItemName"))
            .andExpect(jsonPath("$[0].description").value("Description"))
            .andExpect(jsonPath("$[0].available").value(true));
    }

    @SneakyThrows
    @Test
    public void getItemResearchTest() {
        List<ItemDto> itemDtoList = List.of(itemDto);
        when(itemService.searchItems("It")).thenReturn(itemDtoList);
        mockMvc.perform(get("/items/search?text=It")
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("ItemName"))
            .andExpect(jsonPath("$[0].description").value("Description"))
            .andExpect(jsonPath("$[0].available").value(true));
    }

    @SneakyThrows
    @Test
    public void createItemCommentTest() {
        commentDto = new CommentDto();
        commentDto.setText("Some text");
        User user = new User(2, "User", "user@email.com");
        Item item = ItemMapper.fromItemDto(itemDto);
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        comment.setId(1);
        CommentDtoResponse commentDtoResponse = CommentMapper
            .toCommentDtoResponse(comment);
        when(itemService.createComment(2, 1, commentDto)).thenReturn(commentDtoResponse);
        mockMvc.perform(post("/items/{itemId}/comment", 1)
                .content(objectMapper.writeValueAsString(commentDto))
                .header("X-Sharer-User-Id", 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.authorName").value("User"))
            .andExpect(jsonPath("$.text").value("Some text"))
            .andExpect(jsonPath("$.created").isNotEmpty());
    }

}
