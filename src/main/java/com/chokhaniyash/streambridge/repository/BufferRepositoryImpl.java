package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.entity.BufferEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.Get;

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
}
