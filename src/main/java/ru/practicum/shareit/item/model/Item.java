package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
public class Item {

    private Integer id;
    @NotBlank
    @NotBlank
    private String name;
    @NotBlank
    @NotBlank
    private String description;
    private int owner;
    private Boolean available;

}
