package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemResponseDto createRequest(ItemRequestDto itemRequestDto, Integer userId);
    ItemResponseDto getRequestById(Integer userId, Integer requestId);
    List<ItemResponseDto> getRequestsUser(Integer userId);
    List<ItemResponseDto> getAllRequests(Integer userId, Integer from, Integer size);

}
