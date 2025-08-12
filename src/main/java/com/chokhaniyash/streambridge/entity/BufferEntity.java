package com.chokhaniyash.streambridge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class BufferEntity {
    private String videoId;
    private String email;
    private String name;
    private String userId;
    private String username;

    @DynamoDbPartitionKey
    public String getVideoId(){
        return videoId;
    }
}
