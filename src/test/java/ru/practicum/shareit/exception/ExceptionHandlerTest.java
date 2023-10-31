package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExceptionHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void MessageTest() {
        NotFoundException exception = new NotFoundException("Message");
        ErrorResponse errorResponse = new ErrorResponse("Message");
        assertEquals("Message", exception.getMessage());
        assertEquals(errorResponse.getError(), errorHandler.handleThrowableException(exception).getError());
    }
}
