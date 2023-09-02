package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

    public Item toItemDto(ru.practicum.shareit.item.model.Item item) {
        return new Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public ru.practicum.shareit.item.model.Item fromItemDto(Item itemDto) {
        return new ru.practicum.shareit.item.model.Item(itemDto.getId(),
            itemDto.getName(),
            itemDto.getDescription(),
            0,
            itemDto.getAvailable());
    }

    public List<Item> itemDtoList(List<ru.practicum.shareit.item.model.Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
