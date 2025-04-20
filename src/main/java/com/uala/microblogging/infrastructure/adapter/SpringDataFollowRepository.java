package com.uala.microblogging.infrastructure.adapter;

import com.uala.microblogging.infrastructure.document.FollowerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataFollowRepository extends MongoRepository<FollowerDocument, UUID> {
    List<FollowerDocument> findByFollowerIdsContains(UUID followerId);
}
