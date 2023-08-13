package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Item {

    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private int owner;
    private Boolean available;

}
