package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserService userService;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private ItemResponseDto responseDto;

    @BeforeEach
    public void beforeEach() {
        userDto = new UserDto(1, "User", "user@email.com");
        itemRequestDto = new ItemRequestDto("description");
        userService.createUser(userDto);
    }

    @Test
    public void createRequestTest() {
        responseDto = itemRequestService.createRequest(itemRequestDto, 1);
        assertEquals(responseDto.getId(), 1);
        assertEquals(responseDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    public void getRequestByIdTest() {
        itemRequestService.createRequest(itemRequestDto, 1);
        responseDto = itemRequestService.getRequestById(1, 1);
        assertEquals(responseDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    public void getAllRequestsTest() {
        itemRequestService.createRequest(itemRequestDto, 1);
        List<ItemResponseDto> responseDtoList = itemRequestService.getAllRequests(1, 0, 1);
        assertNotNull(responseDtoList);
        assertEquals(responseDtoList.get(0).getDescription(), itemRequestDto.getDescription());
    }

    @Test
    public void getRequestsUserTest() {
        itemRequestService.createRequest(itemRequestDto, 1);
        List<ItemResponseDto> responseDtoList = itemRequestService.getRequestsUser(1);
        assertNotNull(responseDtoList);
        assertEquals(responseDtoList.get(0).getDescription(), itemRequestDto.getDescription());
    }

}
