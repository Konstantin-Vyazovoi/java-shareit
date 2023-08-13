package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(groups = CreateItemGroup.class)
    private String name;
    @NotBlank(groups = CreateItemGroup.class)
    private String description;
    @NotNull(groups = CreateItemGroup.class)
    private Boolean available;
}