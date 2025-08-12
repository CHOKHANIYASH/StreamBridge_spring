package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    UserEntity insertOne(UserEntity user);
    List<UserEntity> findAll();
    UserEntity findById(String id);
    void deleteById(String id);
}
