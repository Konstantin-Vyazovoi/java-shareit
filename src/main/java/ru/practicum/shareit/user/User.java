package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Valid
@AllArgsConstructor
public class User {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}
