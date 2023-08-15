package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.interfaces.UpdateGroup;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Valid
@AllArgsConstructor
public class UserDto {
    private final Integer id;
    @NotBlank(groups = {CreateGroup.class}, message = "Имя не может быть пустым")
    private String name;
    @Email(groups = {CreateGroup.class,
        UpdateGroup.class}, regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный email")
    @NotBlank(groups = {CreateGroup.class}, message = "Email не может быть пустым")
    private String email;
}
