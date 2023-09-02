package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.item.dto.ItemMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Autowired
    public ItemService(ItemRepository itemStorage, UserRepository userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Transactional
    public List<Item> getItems(int userId) {
        log.info("Получение списка предметов пользователя с id: {}", userId);
        return itemDtoList(itemStorage.findAllByOwnerId(userId));
    }

    @Transactional
    public Item getItem(Integer id) {
        log.info("Получение предмета с id: {}", id);
        Optional<ru.practicum.shareit.item.model.Item> item = itemStorage.findById(id);
        if (item.isEmpty()) throw new NotFoundException("Предмет не найден");
        log.info("Предмет получен : {}", item);
        return toItemDto(item.get());
    }

    @Transactional
    public Item createItem(Item itemDto, int userId) {
        log.info("Создание предмета с id пользователя: {}", userId);
        if (userId > 0 && userStorage.findById(userId).isPresent()) {
            log.info("Создание предмета : {}, ", itemDto);
            ru.practicum.shareit.item.model.Item item = fromItemDto(itemDto);
            item.setOwnerId(userId);
            return toItemDto(itemStorage.save(item));
        } else throw new NotFoundException("Пользователь не найден");
    }

    @Transactional
    public Item updateItem(Integer id, Item itemDto, Integer owner) {
        log.info("Обновление предмета с id пользователя {}, id предмета {}", owner, id);
        Optional<ru.practicum.shareit.item.model.Item> optionalItem = itemStorage.findById(id);
        if (optionalItem.isEmpty()) throw new NotFoundException("Предмет не найден");
        ru.practicum.shareit.item.model.Item item = optionalItem.get();
        if (optionalItem.get().getOwnerId() != owner) throw new NotFoundException("Пользователь не найден");
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        return toItemDto(itemStorage.save(item));
    }

    @Transactional
    public List<Item> searchItems(String searchText) {
        log.info("Поиск предмета по строке: {}", searchText);
        if (searchText.isBlank()) return Collections.emptyList();
        return itemDtoList(itemStorage.
            searchItem
                (searchText));
    }
}