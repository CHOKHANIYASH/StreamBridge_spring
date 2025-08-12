package com.chokhaniyash.streambridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoResponse {
    private String id;
    private String createdAt;
    private String name;
    private String url;
    private String username;
}
