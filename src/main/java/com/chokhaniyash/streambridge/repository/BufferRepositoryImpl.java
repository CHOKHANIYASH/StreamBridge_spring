package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.entity.BufferEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.Get;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class BufferRepositoryImpl implements BufferRepository{
    private final DynamoDbTable<BufferEntity> bufferTable;
    @Override
    public BufferEntity insertOne(BufferEntity buffer) {
        bufferTable.putItem(buffer);
        return buffer;
    }

    @Override
    public BufferEntity findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        BufferEntity buffer = bufferTable.getItem(key);
        return buffer;
    }

    @Override
    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        bufferTable.deleteItem(key);
    }

    @Override
    public List<BufferEntity> findByUserId(String userId) {
        // currently using scan operation, later index can be used
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().consistentRead(true).build();
        PageIterable<BufferEntity> pages  = bufferTable.scan(request);
        List<BufferEntity> buffer = new ArrayList<>();
        pages.stream().forEach(p -> p.items().forEach(item -> {
                    if (item.getUserId().equals(userId))
                        buffer.add(item);
                }
        ));
        return buffer;
    }


}
