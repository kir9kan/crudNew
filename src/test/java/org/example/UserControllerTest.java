package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDto;
import org.example.exception.UserNotFoundException;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserDto testUser = new UserDto(1L,"Joe", "test@Test.ru", 23, LocalDateTime.now());
    private final UserDto testUser2 = new UserDto(2L,"Poe", "test2@Test.ru", 24, LocalDateTime.now());
    private final UserDto invalidUser = new UserDto(null, "invalid", "invalidEmail", -23, null);
    String nullUser = """
    {
        "name": null,
        "email": null,
        "age": null
    }""";

    @Test
    void save_user_with_valid_data_is_success() throws Exception {
        when(userService.saveUser(any(UserDto.class))).thenReturn(testUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))) //Перевод testUser dto в JSON
                .andExpect(status().isCreated()) //Проверка на http-код 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Joe"));
    }

    @Test
    void save_user_with_invalid_data_returns_bad_request_error_and_messages() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid Email"))
                .andExpect(jsonPath("$.age").value("Age must be at least 1"));
    }
    @Test
    void save_user_with_null_data_returns_bad_request_error_and_messages() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullUser))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be null"))
                .andExpect(jsonPath("$.email").value("must not be null"))
                .andExpect(jsonPath("$.age").value("Age must be at least 1"));
    }
    @Test
    void find_user_by_existing_id_is_success() throws Exception {
        when(userService.findUserById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void find_user_by_non_existing_id_returns_not_found() throws Exception {
        when(userService.findUserById(23L)).thenThrow(new UserNotFoundException(23L));

        mockMvc.perform(get("/users/23"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with Id 23 not found"));
    }

    @Test
    void find_all_users_is_success() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(testUser, testUser2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    @Test
    void find_all_users_when_no_users_in_db_returns_empty_list() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
    @Test
    void update_user_with_valid_data_is_success() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenReturn(testUser);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Joe"))
                .andExpect(jsonPath("$.email").value("test@Test.ru"))
                .andExpect(jsonPath("$.age").value(23));
    }

    @Test
    void update_user_with_invalid_data_returns_bad_request_error_and_messages() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid Email"))
                .andExpect(jsonPath("$.age").value("Age must be at least 1"));
    }
    @Test
    void update_non_existent_user_returns_not_found() throws Exception {
        when(userService.updateUser(any(UserDto.class))).thenThrow(new UserNotFoundException(anyLong()));

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_user_with_null_data_returns_bad_request_error_and_messages() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullUser))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be null"))
                .andExpect(jsonPath("$.email").value("must not be null"))
                .andExpect(jsonPath("$.age").value("Age must be at least 1"));
    }
    @Test
    void delete_user_by_existing_id_is_success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_user_by_non_existing_id_returns_not_found() throws Exception {
        doThrow(new UserNotFoundException(23L)).when(userService).deleteUser(23L);

        mockMvc.perform(delete("/users/23"))
                .andExpect(status().isNotFound());
    }

}
