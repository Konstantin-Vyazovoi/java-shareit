package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotBlank(groups = CreateGroup.class)
    private String name;
    @NotBlank(groups = CreateGroup.class)
    private String description;
    @NotNull(groups = CreateGroup.class)
    private Boolean available;
}
