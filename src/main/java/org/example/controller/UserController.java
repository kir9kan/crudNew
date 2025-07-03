package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)//Код 201
    public UserDto saveUser(@Valid @RequestBody UserDto userDto){
        return userService.saveUser(userDto);
    }
    @GetMapping("/{id}")
    public UserDto findUserById(@PathVariable("id") Long id){
        return userService.findUserById(id);
    }
    @GetMapping
    public List<UserDto> findAllUsers(){
        return userService.findAllUsers();
    }
    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)//Код 204
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }
}