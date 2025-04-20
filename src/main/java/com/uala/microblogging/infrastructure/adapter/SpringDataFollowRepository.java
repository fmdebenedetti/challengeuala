package com.uala.microblogging.infrastructure.adapter;

import com.uala.microblogging.infrastructure.document.FollowerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataFollowRepository extends MongoRepository<FollowerDocument, UUID> {
}
