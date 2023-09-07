package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
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
    public List<ItemDto> getItems(@RequestHeader(headerUserId) int userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@RequestHeader(headerUserId) int userId,
                           @PathVariable int id) {
        return itemService.getItem(id, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String itemName) {
        return itemService.searchItems(itemName);
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader(headerUserId) int userId,
                              @Validated(CreateGroup.class) @RequestBody ItemDto item) {
        return itemService.createItem(item, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader(headerUserId) int userId,
                              @RequestBody ItemDto item,
                              @PathVariable int id) {
        return itemService.updateItem(id, item, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestHeader(headerUserId) Integer userId,
                                            @PathVariable Integer itemId,
                                            @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }
}
