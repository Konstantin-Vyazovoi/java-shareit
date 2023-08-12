package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class ItemService {

    @Autowired
    @Qualifier("InMemoryItemStorage")
    private ItemStorage itemStorage;
    @Autowired
    @Qualifier("InMemoryUserStorage")
    private UserStorage userStorage;

    public List<ItemDto> getItems(int userId) {
        return ItemMapper.itemDtoList(itemStorage.getItems(userId));
    }

    public ItemDto getItem(Integer id) {
        return ItemMapper.toItemDto(itemStorage.getItem(id));
    }

    public ItemDto createItem(ItemDto item, int userId) {
        if (userId > 0 && userStorage.getUser(userId) != null) {
            return ItemMapper.toItemDto(itemStorage.createItem(item, userId));
        } else throw new NotFoundException("Пользователь не найден");
    }

    public ItemDto updateItem(Integer id, ItemDto itemDto, Integer owner) {
        if (itemStorage.getItem(id).getOwner() != owner) throw new NotFoundException("Пользователь не найден");
        return ItemMapper.toItemDto(itemStorage.updateItem(id, itemDto));
    }

    public List<ItemDto> searchItems(String itemName) {
        return ItemMapper.itemDtoList(itemStorage.searchItems(itemName.toLowerCase()));
    }
}
