package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.interfaces.CreateGroup;
import ru.practicum.shareit.interfaces.UpdateGroup;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Valid
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    @NotBlank(groups = {CreateGroup.class}, message = "Имя не может быть пустым")
    private String name;
    @Email(groups = {CreateGroup.class,
        UpdateGroup.class}, regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный email")
    @NotBlank(groups = {CreateGroup.class}, message = "Email не может быть пустым")
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;

        UserDto userDto = (UserDto) o;

        if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
        if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
        return email != null ? email.equals(userDto.email) : userDto.email == null;
    }

}
