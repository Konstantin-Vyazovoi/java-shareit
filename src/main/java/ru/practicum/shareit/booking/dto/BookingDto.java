package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.interfaces.CreateGroup;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    @NotNull(groups = CreateGroup.class)
    @Future(groups = CreateGroup.class)
    private LocalDateTime start;
    @NotNull(groups = CreateGroup.class)
    @Future(groups = CreateGroup.class)
    private LocalDateTime end;
    @NotNull(groups = CreateGroup.class)
    private Integer itemId;
}
