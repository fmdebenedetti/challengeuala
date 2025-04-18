package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.domain.repository.FollowRepository;
import com.uala.microblogging.infrastructure.document.FollowerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FollowMongoRepository implements FollowRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findFollowerIdsByUserId(String userId) {
        FollowerDocument doc = mongoTemplate.findById(userId, FollowerDocument.class);
        return doc != null ? doc.getFollowerIds() : List.of();
    }

    @Override
    public void follow(UUID followerId, UUID followeeId) {
        FollowerDocument doc = mongoTemplate.findById(followeeId.toString(), FollowerDocument.class);
        if (doc == null) {
            doc = new FollowerDocument();
            doc.setId(followeeId.toString());
            doc.setFollowerIds(new ArrayList<>());
        }

        List<String> followers = doc.getFollowerIds();
        if (!followers.contains(followerId.toString())) {
            followers.add(followerId.toString());
        }

        mongoTemplate.save(doc);
    }
}