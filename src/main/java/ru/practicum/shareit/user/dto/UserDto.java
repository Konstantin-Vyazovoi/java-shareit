package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Valid
@AllArgsConstructor
public class UserDto {
    private final Integer id;
    @NotBlank(groups = {CreateUserGroup.class}, message = "Имя не может быть пустым")
    private String name;
    @Email(groups = {CreateUserGroup.class}, regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный email")
    @NotBlank(groups = {CreateUserGroup.class}, message = "Email не может быть пустым")
    private String email;
}
