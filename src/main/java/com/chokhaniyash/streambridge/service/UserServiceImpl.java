package com.chokhaniyash.streambridge.service;

import com.chokhaniyash.streambridge.Exceptions.AlreadyExistException;
import com.chokhaniyash.streambridge.Exceptions.NotFoundException;
import com.chokhaniyash.streambridge.dto.request.UserRequest;
import com.chokhaniyash.streambridge.dto.response.UserResponse;
import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import com.chokhaniyash.streambridge.entity.UserEntity;
import com.chokhaniyash.streambridge.repository.UserRepository;
import com.chokhaniyash.streambridge.util.Authorization;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final Authorization authorization;
    @Override
    public UserResponse addUser(UserRequest request) {
        UserEntity existingUser = userRepository.findById(request.getId());
        if(existingUser != null){
            throw new AlreadyExistException("User with the given Id already Exist");
        }
        UserEntity user = convertToEntity(request);
        user = userRepository.insertOne(user);
        return convertToResponse(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public UserResponse getUser() {
        String userId = authorization.getUserId();
        UserEntity user = userRepository.findById(userId);
        if(user == null){
            throw new NotFoundException("User with the given Id does not exist");
        }
        return convertToResponse(user);
    }

    @Override
    public void deleteUser() {
        String userId = authorization.getUserId();
        userRepository.deleteById(userId);
    }

    public UserResponse convertToResponse(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public UserEntity convertToEntity(UserRequest request){
        return UserEntity.builder()
                .id(request.getId())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
    }
}
