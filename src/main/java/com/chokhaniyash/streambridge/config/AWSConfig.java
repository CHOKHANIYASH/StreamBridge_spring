package com.chokhaniyash.streambridge.config;

import com.chokhaniyash.streambridge.entity.BufferEntity;
import com.chokhaniyash.streambridge.entity.UserEntity;
import com.chokhaniyash.streambridge.entity.VideoEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@Configuration
public class AWSConfig {
    @Value("${aws.accessKeyId}")
    private String accessKey;
    @Value("${aws.secretAccessKey}")
    private String secretKey;

    @Bean
    public DynamoDbClient getDynamoDbClient(){
        Region region = Region.AP_SOUTH_1;
        DefaultCredentialsProvider customizedProvider = DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)  // Enable async credential updates.
                .build();
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                                            .region(region)
                                            .credentialsProvider(customizedProvider)
                                            .build();
        return dynamoDbClient;
    }

    @Bean
    public S3Client getS3Client(){
        Region region = Region.AP_SOUTH_1;
        DefaultCredentialsProvider customizedProvider = DefaultCredentialsProvider.builder()
                .asyncCredentialUpdateEnabled(true)  // Enable async credential updates.
                .build();
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(customizedProvider)
                .build();
        return s3Client;
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        DynamoDbEnhancedClient dynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
                                                            .dynamoDbClient(dynamoDbClient)
                                                            .build();
        return dynamoDbEnhancedClient;
    }

    @Bean
    public S3Presigner getS3Presigner(S3Client s3Client){
        S3Presigner s3Presigner = S3Presigner.builder().s3Client(s3Client).build();
        return s3Presigner;
    }

    @Bean
    public DynamoDbTable<VideoEntity> getVideoTable(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        DynamoDbTable<VideoEntity> videoTable = dynamoDbEnhancedClient.table("streambridge_videos", TableSchema.fromBean(VideoEntity.class));
        return videoTable;
    }

    @Bean
    public DynamoDbTable<UserEntity> getUserTable(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        DynamoDbTable<UserEntity> userTable = dynamoDbEnhancedClient.table("streambridge_user",TableSchema.fromBean(UserEntity.class));
        return userTable;
    }

    @Bean
    public DynamoDbTable<BufferEntity> getBufferTable(DynamoDbEnhancedClient dynamoDbEnhancedClient){
        DynamoDbTable<BufferEntity> bufferTable = dynamoDbEnhancedClient.table("streambridge_buffer",TableSchema.fromBean(BufferEntity.class));
        return bufferTable;
    }

}
