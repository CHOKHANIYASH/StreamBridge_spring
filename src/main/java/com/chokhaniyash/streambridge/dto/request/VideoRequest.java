package com.chokhaniyash.streambridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VideoRequest {
    private String id;
    private String name;
//    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "content-type is required")
    @Pattern(regexp = "(?i)^video/mp4$", message = "Only MP4 files are allowed")
    private String contentType;
//    @NotBlank(message = "Email is required")
    private String email;
}
