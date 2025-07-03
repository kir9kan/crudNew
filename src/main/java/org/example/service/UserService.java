package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository =  userRepository;
    }
    public UserDto saveUser(UserDto userDto){
        User user = userRepository.save(dtoToEntity(userDto));
        log.info("User created with Id: {}", user.getId());
        return entityToDto(user);
    }
    public UserDto findUserById(Long id){
        UserDto userDto = userRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> {
                    log.error("User not found with Id: {}", id);
                    return new UserNotFoundException(id);
                });
        log.info("User was found with Id: {}", id);
        return userDto;
    }
    public List<UserDto> findAllUsers(){
        log.info("Retrieving all users");
        return userRepository.findAll()
                .stream()
                .map(this::entityToDto)//Перевод всех Entity в Dto'шки
                .toList();
    }
    public UserDto updateUser(UserDto userDto){
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> {
                    log.error("User to update not found with Id: {}", userDto.getId());
                    return new UserNotFoundException(userDto.getId());
                });
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        log.info("User was updated with Id: {}", user.getId());
        return entityToDto(userRepository.save(user));
    }
    public void  deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User to delete not found with Id: {}", id);
                    return new UserNotFoundException(id);
                });
        userRepository.delete(user);
        log.info("User was deleted with id: {}", id);
    }
    public UserDto entityToDto(User user){
        return new UserDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt());
    }
    public User dtoToEntity(UserDto userDto){
        return new User(userDto.getName(),
                userDto.getEmail(),
                userDto.getAge());
    }
}
