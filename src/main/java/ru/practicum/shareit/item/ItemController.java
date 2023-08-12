package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CreateGroup;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable int id) {
        return itemService.getItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String itemName) {
        return itemService.searchItems(itemName);
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Validated(CreateGroup.class) @RequestBody ItemDto item) {
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Valid @RequestBody ItemDto item,
                              @PathVariable int id) {
        return itemService.updateItem(id, item, userId);
    }
}
