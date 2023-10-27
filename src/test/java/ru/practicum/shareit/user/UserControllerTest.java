package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void createValidUser() {

        UserDto createUserDto = new UserDto(1, "User", "user@email.com");
        Mockito.when(userService.createUser(createUserDto)).thenReturn(createUserDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(createUserDto.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createUserDto.getName()));

    }

    @SneakyThrows
    @Test
    void createUserWithInvalidName() {

        UserDto createUserDto = new UserDto(1, " ", "user@email.com");
        Mockito.when(userService.createUser(createUserDto)).thenReturn(createUserDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void createUserWithInvalidEmail() {

        UserDto createUserDto = new UserDto(1, "User", "user.com");
        Mockito.when(userService.createUser(createUserDto)).thenReturn(createUserDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @SneakyThrows
    @Test
    void updateValidUser() {

        Integer userId = 1;
        UserDto updateUserDto = new UserDto(1, "UpdateUser", "update@email.com");
        Mockito.when(userService.updateUser(1, updateUserDto)).thenReturn(updateUserDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updateUserDto.getEmail()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updateUserDto.getName()));

    }

    @SneakyThrows
    @Test
    void getAllUsersTest() {

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @SneakyThrows
    @Test
    void getUserByIdTest() {

        int userId = 1;
        mockMvc.perform(get("/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
