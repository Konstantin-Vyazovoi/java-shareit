package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExceptionHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();
    private UserMapper userMapper;
    private ItemMapper itemMapper;
    private ItemRequestMapper itemRequestMapper;
    private BookingMapper bookingMapper;

    @Test
    public void getTest() {

        NotFoundException exception = new NotFoundException("Message");
        ErrorResponse errorResponse = new ErrorResponse("Message");
        assertEquals("Message", exception.getMessage());
        assertEquals(errorResponse.getError(), errorHandler.handleThrowableException(exception).getError());
    }
}
