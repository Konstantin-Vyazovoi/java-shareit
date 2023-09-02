package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.item.dto.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final String headerUserId = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> getItems(@RequestHeader(headerUserId) int userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{id}")
    public Item getItem(@PathVariable int id) {
        return itemService.getItem(id);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam("text") String itemName) {
        return itemService.searchItems(itemName);
    }

    @PostMapping()
    public Item createItem(@RequestHeader(headerUserId) int userId,
                           @Validated(CreateGroup.class) @RequestBody Item item) {
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader(headerUserId) int userId,
                           @RequestBody Item item,
                           @PathVariable int id) {
        return itemService.updateItem(id, item, userId);
    }
}
