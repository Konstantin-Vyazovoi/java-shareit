package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingServiceImpl bookingService;

    private User booker;
    private Item item;
    private BookingDto bookingDto;
    private BookingResponseDto responseDto;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void beforeEach() {
        booker = new User(1, "User", "user@email.com");
        item = new Item(1, "ItemName", "Description", 1, true, null);
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);
        bookingDto = new BookingDto(start, end, 1);
        responseDto = BookingResponseDto.builder()
            .id(1)
            .status(BookingStatus.WAITING)
            .booker(booker)
            .item(item)
            .start(start)
            .end(end)
            .build();

    }

    @SneakyThrows
    @Test
    public void createBookingTest() {
        when(bookingService.createBooking(bookingDto, 1)).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                .content(objectMapper.writeValueAsString(bookingDto))
                .header("X-Sharer-User-Id", booker.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.item.id").value(1))
            .andExpect(jsonPath("$.item.name").value("ItemName"))
            .andExpect(jsonPath("$.status").value("WAITING"))
            .andExpect(jsonPath("$.start").isNotEmpty())
            .andExpect(jsonPath("$.end").isNotEmpty())
            .andExpect(jsonPath("$.booker.id").value(1))
            .andExpect(jsonPath("$.booker.name").value("User"));
    }

    @SneakyThrows
    @Test
    public void updateBookingTest() {
        responseDto.setStatus(BookingStatus.APPROVED);
        when(bookingService.updateBookingStatus(1, 1, true)).thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", 1)
                .header("X-Sharer-User-Id", booker.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.item.id").value(1))
            .andExpect(jsonPath("$.item.name").value("ItemName"))
            .andExpect(jsonPath("$.status").value("APPROVED"))
            .andExpect(jsonPath("$.start").isNotEmpty())
            .andExpect(jsonPath("$.end").isNotEmpty())
            .andExpect(jsonPath("$.booker.id").value(1))
            .andExpect(jsonPath("$.booker.name").value("User"));
    }

    @SneakyThrows
    @Test
    public void getBookingByIdTest() {
        when(bookingService.getById(1, 1)).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                .header("X-Sharer-User-Id", booker.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.item.id").value(1))
            .andExpect(jsonPath("$.item.name").value("ItemName"))
            .andExpect(jsonPath("$.status").value("WAITING"))
            .andExpect(jsonPath("$.start").isNotEmpty())
            .andExpect(jsonPath("$.end").isNotEmpty())
            .andExpect(jsonPath("$.booker.id").value(1))
            .andExpect(jsonPath("$.booker.name").value("User"));
    }

    @SneakyThrows
    @Test
    public void getBookerBookingListTest() {
        when(bookingService.getBookings(1, "ALL", 1, 2)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/bookings")
                .param("state", "ALL")
                .param("from", "1")
                .param("size", "2")
                .header("X-Sharer-User-Id", booker.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].item.id").value(1))
            .andExpect(jsonPath("$[0].item.name").value("ItemName"))
            .andExpect(jsonPath("$[0].status").value("WAITING"))
            .andExpect(jsonPath("$[0].start").isNotEmpty())
            .andExpect(jsonPath("$[0].end").isNotEmpty())
            .andExpect(jsonPath("$[0].booker.id").value(1))
            .andExpect(jsonPath("$[0].booker.name").value("User"));
    }

    @SneakyThrows
    @Test
    public void getOwnerBookingListTest() {
        when(bookingService.getBookingsByOwner(1, "ALL", 1, 2)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/bookings/owner")
                .param("state", "ALL")
                .param("from", "1")
                .param("size", "2")
                .header("X-Sharer-User-Id", booker.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].item.id").value(1))
            .andExpect(jsonPath("$[0].item.name").value("ItemName"))
            .andExpect(jsonPath("$[0].status").value("WAITING"))
            .andExpect(jsonPath("$[0].start").isNotEmpty())
            .andExpect(jsonPath("$[0].end").isNotEmpty())
            .andExpect(jsonPath("$[0].booker.id").value(1))
            .andExpect(jsonPath("$[0].booker.name").value("User"));
    }
}
