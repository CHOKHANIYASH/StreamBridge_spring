package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.entity.BufferEntity;

import java.nio.Buffer;

public interface BufferRepository {
    BufferEntity insertOne(BufferEntity buffer);
    BufferEntity findById(String id);
    void deleteById(String id);
}
