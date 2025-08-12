package com.chokhaniyash.streambridge.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class VideoEntity {
    private String id;
    private String createdAt;
    private String name;
    private String url;
    private String userId;
    private String username;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "userId-index")
    public String getUserId() {
        return userId;
    }

}
