package com.chokhaniyash.streambridge.controller;

import com.chokhaniyash.streambridge.dto.request.UserRequest;
import com.chokhaniyash.streambridge.dto.response.UserResponse;
import com.chokhaniyash.streambridge.entity.UserEntity;
import com.chokhaniyash.streambridge.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<UserEntity> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping
    public UserResponse getUser(){
        return userService.getUser();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@Valid @RequestBody UserRequest request){
        return userService.addUser(request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(){
        userService.deleteUser();
    }
}
