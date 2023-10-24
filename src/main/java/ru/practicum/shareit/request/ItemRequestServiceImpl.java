package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService{

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemResponseDto createRequest(ItemRequestDto itemRequestDto, Integer userId) {
        log.info("Создание запроса пользователя с id: {}", userId);
        User requestor = validateUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(requestor);
        return ItemRequestMapper.toResponse(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemResponseDto getRequestById(Integer userId, Integer requestId) {
        log.info("Получение запроса пользователя с id: {}", userId);
        validateUser(userId);
        ItemRequest itemRequest = validateRequest(requestId);
        ItemResponseDto itemResponseDto = ItemRequestMapper.toResponse(itemRequest);
        addItems(itemResponseDto);
        return itemResponseDto;
    }

    @Override
    public List<ItemResponseDto> getRequestsUser(Integer userId) {
        log.info("Получение списка запросов пользователя с id: {}", userId);
        validateUser(userId);
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorId(userId);
        List<ItemResponseDto> itemResponseDtos = ItemRequestMapper.toResponseList(itemRequestList);
        itemResponseDtos.forEach(this::addItems);
        return itemResponseDtos;
    }

    @Override
    public List<ItemResponseDto> getAllRequests(Integer userId, Integer from, Integer size) {
        log.info("Получение списка всех запросов от пользователя с id: {}", userId);
        if (from.intValue() < 0 || size.intValue() < 0)
            throw new BadRequestException(
                String.format("Не правильные значения from = %s, size = %s" , from, size));
        validateUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequestList = itemRequestRepository
            .findAllByRequestorIdIsNotOrderByCreated(userId, pageable)
            .toList();
        List<ItemResponseDto> itemResponseDtos = ItemRequestMapper.toResponseList(itemRequestList);
        itemResponseDtos.forEach(this::addItems);
        return itemResponseDtos;
    }

    private User validateUser(Integer id) {
        if (id == null) throw new ValidateException(String.format("Не правильный id: %s", id));
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException(String.format("Пользователь с id %s не найден", id));
        return user.get();
    }

    private ItemRequest validateRequest(Integer requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isEmpty())
            throw new NotFoundException(String.format("Запрос с id %s не найден", requestId));
        return itemRequest.get();
    }

    private void addItems (ItemResponseDto responseDto) {
        List<Item> items = itemRepository.findAllByRequestId(responseDto.getId());
        responseDto.setItems(ItemMapper.itemDtoList(items, null));
    }

}
