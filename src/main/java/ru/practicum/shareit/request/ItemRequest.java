package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequest {
    private Integer id;
    @NotBlank
    private String description;
    private LocalDate created;
    private User requestor;
}
