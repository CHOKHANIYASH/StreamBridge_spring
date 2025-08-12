package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import com.chokhaniyash.streambridge.entity.VideoEntity;

import java.util.List;

public interface VideoRepository {
    VideoEntity findById(String id);
    List<VideoEntity> findByUserId(String userId);
    List<VideoEntity> findAll();
    VideoEntity insertOne(VideoEntity video);
    void deleteById(String id);
}
