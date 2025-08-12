package com.chokhaniyash.streambridge.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.internal.GetAttr;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.security.Key;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class S3Util {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    @Value("${aws.s3.buffer_bucket}")
    private String s3BufferBucket;
    @Value("${aws.s3.cloudfront_bucket}")
    private String s3CloudfrontBucket;
    @Value("${AWS_CLOUDFRONT_URL}")
    private String cloudfrontUrl;

    public String getPresignedUploadUrl(String objectKey){
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BufferBucket)
                .key(objectKey)
                .build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

        String presignedUrl = presignedPutObjectRequest.url().toString();
        return presignedUrl;
    }

    public String getObjectUrl(String objectKey){
        return cloudfrontUrl + "/" + objectKey;
    }

    public boolean checkObjectExist(String objectKey) {
        return checkObjectExist(objectKey, s3CloudfrontBucket);
    }

    public boolean checkObjectExist(String objectKey,String bucket) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void removeObject(String objectKey) {
        deleteIfExists(objectKey, s3BufferBucket);
        deleteIfExists(objectKey, s3CloudfrontBucket);
    }

    private void deleteIfExists(String objectKey, String bucket) {
        if (checkObjectExist(objectKey, bucket)) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
    }
}