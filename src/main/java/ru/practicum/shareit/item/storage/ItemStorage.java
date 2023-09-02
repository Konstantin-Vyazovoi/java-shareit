package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.Item;

import java.util.List;

public interface ItemStorage {
    ru.practicum.shareit.item.model.Item getItem(int id);

    List<ru.practicum.shareit.item.model.Item> getItems(int userId);

    ru.practicum.shareit.item.model.Item createItem(Item item, int userId);

    ru.practicum.shareit.item.model.Item updateItem(int id, Item item);

    List<ru.practicum.shareit.item.model.Item> searchItems(String itemName);
}
