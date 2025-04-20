package com.uala.microblogging.infrastructure.adapter;

import com.uala.microblogging.infrastructure.document.TweetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataTweetRepository extends MongoRepository<TweetDocument, UUID> {
    List<TweetDocument> findByUserIdInOrderByCreatedAtDesc(List<UUID> userIds);
}
