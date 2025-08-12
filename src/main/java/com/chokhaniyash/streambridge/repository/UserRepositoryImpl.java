package com.chokhaniyash.streambridge.repository;

import com.chokhaniyash.streambridge.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteRequest;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{
    DynamoDbTable<UserEntity> userTable;

    @Override
    public UserEntity insertOne(UserEntity user) {
        userTable.putItem(user);
        return user;
    }

    @Override
    public List<UserEntity> findAll() {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().consistentRead(true).build();
        PageIterable<UserEntity> pages = userTable.scan(request);
        List<UserEntity> users = new ArrayList<>();
        pages.stream().forEach(p -> p.items().forEach(item -> users.add(item)) );
        return users;
    }

    @Override
    public UserEntity findById(String id) {
        UserEntity user = userTable.getItem(Key.builder().partitionValue(id).build());
        return user;
    }

    @Override
    public void deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        userTable.deleteItem(key);
    }

}
