package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.dto.response.VideoResponse;
import com.chokhaniyash.streambridge.entity.VideoEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class VideoRepositoryImpl implements VideoRepository{
    DynamoDbTable<VideoEntity> videoTable;

    @Override
    public VideoEntity findById(String id) {
        VideoEntity video = videoTable.getItem(Key.builder().partitionValue(id).build());
        return video;
    }

    @Override
    public List<VideoEntity> findByUserId(String userId) {
        Key key = Key.builder().partitionValue(userId).build();

        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
        QueryEnhancedRequest request = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();

        DynamoDbIndex<VideoEntity> userIdIndex = videoTable.index("userId-index");
        SdkIterable<Page<VideoEntity>> pages = userIdIndex.query(request);
        List<VideoEntity> videos = new ArrayList<>();
        pages.stream().forEach(page -> page.items().forEach(item -> videos.add(item)));
        return videos;
    }

    @Override
    public List<VideoEntity> findAll() {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().consistentRead(true).build();
        PageIterable<VideoEntity> pages = videoTable.scan(request);
        List<VideoEntity> videos = new ArrayList<>();
        pages.stream().forEach(p -> p.items().forEach(item -> videos.add(item)));
        return videos;
    }

    @Override
    public VideoEntity insertOne(VideoEntity video) {
        videoTable.putItem(video);
        return video;
    }

    @Override
    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        videoTable.deleteItem(key);
    }
}
