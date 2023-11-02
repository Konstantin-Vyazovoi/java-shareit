package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

    public ItemDto toItemDto(Item item, List<CommentDtoResponse> comments) {
        return new ItemDto(item.getId(),
            item.getName(),
            item.getDescription(),
            item.getAvailable(),
            null,
            null,
            comments,
            item.getRequestId());
    }

    public Item fromItemDto(ItemDto itemDto) {
        return new Item(itemDto.getId(),
            itemDto.getName(),
            itemDto.getDescription(),
            0,
            itemDto.getAvailable(),
            itemDto.getRequestId());
    }

    public List<ItemDto> itemDtoList(List<Item> items, List<CommentDtoResponse> comments) {
        return items
            .stream()
            .map((Item item) -> toItemDto(item, comments))
            .collect(Collectors.toList());
    }
}
