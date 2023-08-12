package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item getItem(int id);

    List<Item> getItems(int userId);

    Item createItem(ItemDto item, int userId);

    Item updateItem(int id, ItemDto item);

    List<Item> searchItems(String itemName);
}
