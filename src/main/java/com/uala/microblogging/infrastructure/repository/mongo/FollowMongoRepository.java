package com.uala.microblogging.infrastructure.repository.mongo;

import com.uala.microblogging.domain.repository.FollowRepository;
import com.uala.microblogging.infrastructure.adapter.SpringDataFollowRepository;
import com.uala.microblogging.infrastructure.document.FollowerDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FollowMongoRepository implements FollowRepository {

    private final SpringDataFollowRepository repository;

    @Override
    public void follow(UUID followerId, UUID followeeId) {
        FollowerDocument doc = repository.findById(followeeId)
                .orElse(FollowerDocument.builder()
                        .id(followeeId)
                        .followerIds(new ArrayList<>())
                        .build());

        List<UUID> ids = new ArrayList<>(doc.getFollowerIds());
        if (!ids.contains(followerId)) {
            ids.add(followerId);
            doc.setFollowerIds(ids);
            repository.save(doc);
        }
    }

    @Override
    public List<UUID> findFollowerIdsByUserId(UUID followerId) {
        return repository.findById(followerId)
                .map(FollowerDocument::getFollowerIds)
                .orElse(Collections.emptyList());
    }
}