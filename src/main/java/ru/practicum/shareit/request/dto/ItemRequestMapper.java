package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
            .description(itemRequestDto.getDescription())
            .created(LocalDateTime.now())
            .build();
    }

    public static ItemResponseDto toResponse(ItemRequest itemRequest) {
        return ItemResponseDto.builder()
            .id(itemRequest.getId())
            .description(itemRequest.getDescription())
            .created(itemRequest.getCreated())
            .build();
    }

    public static List<ItemResponseDto> toResponseList(List<ItemRequest> itemRequestList) {
        return itemRequestList
            .stream()
            .map((ItemRequestMapper::toResponse))
            .collect(Collectors.toList());
    }
}
