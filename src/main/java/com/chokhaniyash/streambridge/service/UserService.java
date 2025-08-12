package com.chokhaniyash.streambridge.service;

import com.chokhaniyash.streambridge.dto.request.UserRequest;
import com.chokhaniyash.streambridge.dto.response.UserResponse;
import com.chokhaniyash.streambridge.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserResponse addUser(UserRequest request);
    List<UserEntity> getAllUsers();
    UserResponse getUser();
    void deleteUser();
}
