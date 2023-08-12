package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("InMemoryItemStorage")
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();
    private int idGenerated = 0;

    @Override
    public Item getItem(int id) {
        return items.get(id);
    }

    @Override
    public List<Item> getItems(int userId) {
        return new ArrayList<>(items.values())
            .stream()
            .filter(i -> i.getOwner() == userId)
            .collect(Collectors.toList());
    }

    @Override
    public Item createItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.fromItemDto(itemDto);
        item.setId(generateId());
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(int id, ItemDto itemDto) {
        if (items.containsKey(id)) {
            Item item = items.get(id);
            if (itemDto.getName() != null) item.setName(itemDto.getName());
            if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
            if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
            return item;
        } else throw new NotFoundException("Предмет не найден");
    }

    @Override
    public List<Item> searchItems(String itemName) {
        if (itemName.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> collect = new ArrayList<>(items.values())
            .stream()
            .filter(i -> i.getName().toLowerCase().contains(itemName)
                || i.getDescription().toLowerCase().contains(itemName))
            .filter(Item::getAvailable)
            .collect(Collectors.toList());
        return collect;
    }

    private int generateId() {
        return ++idGenerated;
    }
}
