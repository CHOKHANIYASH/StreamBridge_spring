package com.chokhaniyash.streambridge.controller;

import com.chokhaniyash.streambridge.dto.request.VideoRequest;
import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import com.chokhaniyash.streambridge.service.VideoService;
import com.chokhaniyash.streambridge.util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/videos")
public class VideoController {
    private final VideoService videoService;
    private final EmailUtil emailUtil;

    @GetMapping("/email")
    public void sendEmail(){
        Map<String, Object> model = new HashMap<>();
        model.put("username", "Yash");
        model.put("name", "My Awesome Video");
        emailUtil.sendSuccessMail("yashchokhani95@gmail.com","Test email",model);
    }

    @GetMapping("/email/fail")
    public void sendFailedEmail(){
        Map<String, Object> model = new HashMap<>();
        model.put("username", "Yash");
        model.put("name", "My Awesome Video");
        emailUtil.sendFailedMail("yashchokhani95@gmail.com","Test email",model);
    }

    @GetMapping("/all")
    public List<VideoResponse> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public VideoResponse getVideo(@PathVariable("id") String id){
        return videoService.getVideo(id);
    }

    @GetMapping("/user/{userId}")
    public List<VideoResponse> getVideosByUserId(@PathVariable("userId") String userId){
        return videoService.getVideosByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> getUploadUrl(@Valid @RequestBody VideoRequest request){
        return videoService.getUploadUrl(request);
    }

    @PostMapping("/success")
    public VideoResponse addVideo(@RequestBody VideoRequest request){
        //TO DO Add admin authorization
        return videoService.addVideo(request.getId());
    }

    @PostMapping("/fail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadFail(@RequestBody VideoRequest request){
        //TO DO Add admin authorization
        videoService.uploadFail(request.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(@RequestBody VideoRequest request){
        videoService.deleteVideo(request.getId());
    }

}
