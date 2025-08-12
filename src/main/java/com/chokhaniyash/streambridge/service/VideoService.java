package com.chokhaniyash.streambridge.service;

import com.chokhaniyash.streambridge.dto.request.VideoRequest;
import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VideoService {
    List<VideoResponse> getVideosByUserId(String userId);
    VideoResponse getVideo(String id);
    List<VideoResponse> getAllVideos();
    String getPresignedUploadUrl(String key);
    ResponseEntity<?> getUploadUrl(VideoRequest request);
    VideoResponse addVideo(String videoId);
    void uploadFail(String videoId);
    void deleteVideo(String videoId);
}
