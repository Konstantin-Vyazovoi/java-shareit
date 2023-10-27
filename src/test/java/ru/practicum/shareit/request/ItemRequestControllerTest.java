package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequestDto itemRequestDto;
    private ItemResponseDto responseDto;
    private ItemDto itemDto;
    private LocalDateTime dateTime;

    @BeforeEach
    public void beforeEach() {
        dateTime = LocalDateTime.now().minusDays(2);
        itemDto = new ItemDto(1,
            "Item",
            "Some description",
            true,
            null,
            null,
            new ArrayList<>(),
            1);
        itemRequestDto = new ItemRequestDto("Some description");
        responseDto = new ItemResponseDto(1, "Some description", dateTime, List.of(itemDto));
    }

    @SneakyThrows
    @Test
    public void createRequestTest() {
        when(itemRequestService.createRequest(itemRequestDto, 1)).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                .content(objectMapper.writeValueAsString(itemRequestDto))
                .header("X-Sharer-User-Id", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Some description"))
            .andExpect(jsonPath("$.items").isNotEmpty());
    }

    @SneakyThrows
    @Test
    public void getRequestByIdTest() {
        when(itemRequestService.getRequestById(1, 1)).thenReturn(responseDto);

        mockMvc.perform(get("/requests/{requestId}", 1)
                .header("X-Sharer-User-Id", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("Some description"))
            .andExpect(jsonPath("$.items").isNotEmpty());
    }

    @SneakyThrows
    @Test
    public void getAllRequestsTest() {
        when(itemRequestService.getAllRequests(1, 0, 2)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests/all?from=0&size=2")
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].description").value("Some description"))
            .andExpect(jsonPath("$[0].items").isNotEmpty());
    }

    @SneakyThrows
    @Test
    public void getRequestsUserTest() {
        when(itemRequestService.getRequestsUser(1)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].description").value("Some description"))
            .andExpect(jsonPath("$[0].items").isNotEmpty());
    }

}
