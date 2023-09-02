package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, ru.practicum.shareit.item.model.Item> itemsMap = new HashMap<>();
    private final Map<Integer, List<ru.practicum.shareit.item.model.Item>> userItemIndex = new LinkedHashMap<>();
    private int idGenerated = 0;

    @Override
    public ru.practicum.shareit.item.model.Item getItem(int id) {
        return itemsMap.get(id);
    }

    @Override
    public List<ru.practicum.shareit.item.model.Item> getItems(int userId) {
        return userItemIndex.get(userId);
    }

    @Override
    public ru.practicum.shareit.item.model.Item createItem(Item itemDto, int userId) {
        ru.practicum.shareit.item.model.Item item = ItemMapper.fromItemDto(itemDto);
        item.setId(generateId());
        item.setOwnerId(userId);
        itemsMap.put(item.getId(), item);
        final List<ru.practicum.shareit.item.model.Item> items = userItemIndex.computeIfAbsent(item.getOwnerId(), k -> new ArrayList<>());
        items.add(item);
        userItemIndex.put(userId, items);
        return item;
    }

    @Override
    public ru.practicum.shareit.item.model.Item updateItem(int id, Item itemDto) {
        ru.practicum.shareit.item.model.Item item = itemsMap.get(id);
        if (item != null) {
            String name = itemDto.getName();
            String description = itemDto.getDescription();
            if (name != null && !name.isBlank()) item.setName(name);
            if (description != null && !description.isBlank()) item.setDescription(description);
            if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
            return item;
        } else throw new NotFoundException("Предмет не найден");
    }

    @Override
    public List<ru.practicum.shareit.item.model.Item> searchItems(String itemName) {
        List<ru.practicum.shareit.item.model.Item> collect = itemsMap.values()
            .stream()
            .filter(i -> i.getAvailable().equals(true)
                && (i.getName().toLowerCase().contains(itemName)
                || i.getDescription().toLowerCase().contains(itemName)))
            .collect(Collectors.toList());
        return collect;
    }

    private int generateId() {
        return ++idGenerated;
    }
}

