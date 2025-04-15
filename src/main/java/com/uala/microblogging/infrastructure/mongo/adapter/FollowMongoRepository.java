package com.uala.microblogging.infrastructure.mongo.adapter;

import com.uala.microblogging.domain.repository.FollowRepository;
import com.uala.microblogging.infrastructure.mongo.document.FollowerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowMongoRepository implements FollowRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findFollowerIdsByUserId(String userId) {
        FollowerDocument doc = mongoTemplate.findById(userId, FollowerDocument.class);
        return doc != null ? doc.getFollowerIds() : List.of();
    }
}