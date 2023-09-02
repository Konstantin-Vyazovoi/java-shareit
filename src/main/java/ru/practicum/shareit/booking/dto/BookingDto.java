package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class BookingDto {
    private Integer id;
    @NotNull(groups = CreateGroup.class)
    @Future(groups = CreateGroup.class)
    private LocalDateTime start;
    @NotNull(groups = CreateGroup.class)
    @Future(groups = CreateGroup.class)
    private LocalDateTime end;
    @NotNull(groups = CreateGroup.class)
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;
}
