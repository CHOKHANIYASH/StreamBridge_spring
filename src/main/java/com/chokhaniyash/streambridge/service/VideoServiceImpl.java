package com.chokhaniyash.streambridge.service;

import com.chokhaniyash.streambridge.Exceptions.AccessDeniedException;
import com.chokhaniyash.streambridge.Exceptions.NotFoundException;
import com.chokhaniyash.streambridge.dto.request.VideoRequest;
import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import com.chokhaniyash.streambridge.entity.BufferEntity;
import com.chokhaniyash.streambridge.entity.UserEntity;
import com.chokhaniyash.streambridge.entity.VideoEntity;
import com.chokhaniyash.streambridge.repository.BufferRepository;
import com.chokhaniyash.streambridge.repository.UserRepository;
import com.chokhaniyash.streambridge.repository.VideoRepository;
import com.chokhaniyash.streambridge.util.Authorization;
import com.chokhaniyash.streambridge.util.EmailUtil;
import com.chokhaniyash.streambridge.util.S3Util;
import jakarta.mail.MessagingException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.method.MethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final BufferRepository bufferRepository;
    private final S3Util s3Util;
    private final Authorization authorization;
    private final EmailUtil emailUtil;
    @Override
    public List<VideoResponse> getVideosByUserId(String userId) {
        UserEntity user = userRepository.findById(userId);
        if(user == null){
            throw new NotFoundException("User with the given Id does not exist");
        }
        List<VideoEntity> videos = videoRepository.findByUserId(userId);
        return videos.stream().map(video -> convertToResponse(video)).collect(Collectors.toList());
    }

    @Override
    public VideoResponse getVideo(String id) {
        VideoEntity video = videoRepository.findById(id);
        if(video == null){
            throw new NotFoundException("Video with the given id does not exist");
        }
        return convertToResponse(video);
    }

    @Override
    public List<VideoResponse> getAllVideos() {
        List<VideoEntity> videos = videoRepository.findAll();
        return videos.stream().map(video -> convertToResponse(video)).collect(Collectors.toList());
    }

    @Override
    public String getPresignedUploadUrl(String key) {
        return s3Util.getPresignedUploadUrl(key);
    }

    @Override
    public ResponseEntity<?> getUploadUrl(VideoRequest request) {
        String videoId = UUID.randomUUID().toString() +"."+ request.getContentType().split("/")[1]; // Generating the video id
        String presignedUploadUrl = getPresignedUploadUrl(videoId);
        String userId = authorization.getUserId();

        BufferEntity buffer = BufferEntity.builder()
                .videoId(videoId)
                .name(request.getName())
                .email(request.getEmail())
                .userId(userId)
                .username(request.getUsername())
                .build();
        bufferRepository.insertOne(buffer);
        Map<String, Object> response = new HashMap<>();
        response.put("key", videoId);
        response.put("url", presignedUploadUrl);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public VideoResponse addVideo(String videoId) {
        boolean videoTranscodedSuccessfully = s3Util.checkObjectExist(videoId);
        if(!videoTranscodedSuccessfully){
            throw new NotFoundException("Video with the given id not found. Either the video is not uploaded or transcoding unsuccessful");
        }
        BufferEntity buffer = bufferRepository.findById(videoId);
        if(buffer == null){
            throw new NotFoundException("Video not uploaded properly. Try again");
        }
        bufferRepository.deleteById(videoId);
        String videoUrl = s3Util.getObjectUrl(videoId);
        VideoEntity video = VideoEntity.builder()
                .id(videoId)
                .createdAt(LocalDateTime.now().toString())
                .name(buffer.getName())
                .url(videoUrl)
                .userId(buffer.getUserId())
                .username(buffer.getUsername())
                .build();
        videoRepository.insertOne(video);
        String subject = buffer.getName() + "Uploaded Successfully";
        Map<String, Object> model = new HashMap<>();
        model.put("username", buffer.getUsername());
        model.put("name", buffer.getName());
        emailUtil.sendSuccessMail(buffer.getEmail(),subject,model);
        return convertToResponse(video);
    }

    @Override
    public void uploadFail(String videoId) {
        BufferEntity buffer = bufferRepository.findById(videoId);
        if(buffer == null)
            return;
        bufferRepository.deleteById(videoId);
        s3Util.removeObject(videoId);
        String subject = buffer.getName() + " Uploading Failed";
        Map<String, Object> model = new HashMap<>();
        model.put("username", buffer.getUsername());
        model.put("name", buffer.getName());
        emailUtil.sendFailedMail(buffer.getEmail(),subject,model);
    }

    @Override
    public void deleteVideo(String videoId) {
        String userId = authorization.getUserId();
        VideoEntity video = videoRepository.findById(videoId);
        if(!userId.equals(video.getUserId())){
            throw new AccessDeniedException("User does not have access to delete this video");
        }
        videoRepository.deleteById(videoId);
        s3Util.removeObject(videoId);
    }


    public VideoResponse convertToResponse(VideoEntity video){
        return VideoResponse.builder()
                .id(video.getId())
                .createdAt(video.getCreatedAt())
                .name(video.getName())
                .url(video.getUrl())
                .username(video.getUsername())
                .build();
    }

}
