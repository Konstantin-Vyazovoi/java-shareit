package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;
    private final String headerUserId = "X-Sharer-User-Id";

    @Autowired
    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemResponseDto createRequest(@RequestHeader(headerUserId) Integer userId,
                                         @RequestBody @Validated(CreateGroup.class) ItemRequestDto itemRequestDto) {
                                         @RequestBody @Validated(CreateGroup.class)ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запрса: {}", itemRequestDto);
        return requestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping("/{requestId}")
    public ItemResponseDto getRequestById(@RequestHeader(headerUserId) Integer userId,
                                          @PathVariable("requestId") Integer requestId) {
        log.info("Получен запрос от пользователя с id: {} на получение запрса с id: {}", userId, requestId);
        return requestService.getRequestById(userId, requestId);
    }

    @GetMapping
    public List<ItemResponseDto> getRequestsUser(@RequestHeader(headerUserId) Integer userId) {
        log.info("Получен запрос получения списка запросов от пользователя с id: {}", userId);
        return requestService.getRequestsUser(userId);
    }

    @GetMapping("/all")
    public List<ItemResponseDto> getAllRequests(@RequestHeader(headerUserId) Integer userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "100") int size) {
        log.info("Получен запрос получения списка запросов от пользователя с id: {}", userId);
        return requestService.getAllRequests(userId, from, size);
    }
}
