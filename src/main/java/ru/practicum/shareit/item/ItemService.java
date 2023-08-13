package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public List<ItemDto> getItems(int userId) {
        log.info("Получение списка предметов пользователя с id: {}", userId);
        return ItemMapper.itemDtoList(itemStorage.getItems(userId));
    }

    public ItemDto getItem(Integer id) {
        log.info("Получение предмета с id: {}", id);
        Item item = itemStorage.getItem(id);
        log.info("Предмет получен : {}", item);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        return itemDto;
    }

    public ItemDto createItem(ItemDto item, int userId) {
        log.info("Создание предмета с id пользователя: {}", userId);
        if (userId > 0 && userStorage.getUser(userId) != null) {
            log.info("Создание предмета : {}, ", item);
            return ItemMapper.toItemDto(itemStorage.createItem(item, userId));
        } else throw new NotFoundException("Пользователь не найден");
    }

    public ItemDto updateItem(Integer id, ItemDto itemDto, Integer owner) {
        log.info("Обновление предмета с id пользователя {}, id предмета {}", owner, id);
        if (itemStorage.getItem(id).getOwner() != owner) throw new NotFoundException("Пользователь не найден");
        return ItemMapper.toItemDto(itemStorage.updateItem(id, itemDto));
    }

    public List<ItemDto> searchItems(String itemName) {
        log.info("Поиск предмета по строке: {}", itemName);
        if (itemName.isBlank()) return Collections.emptyList();
        return ItemMapper.itemDtoList(itemStorage.searchItems(itemName.toLowerCase()));
    }
}