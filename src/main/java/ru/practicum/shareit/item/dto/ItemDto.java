package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private Integer id;
    @NotBlank(groups = CreateGroup.class)
    private String name;
    @NotBlank(groups = CreateGroup.class)
    private String description;
    @NotNull(groups = CreateGroup.class)
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentDtoResponse> comments;
    private Integer requestId;
}