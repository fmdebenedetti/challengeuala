package com.uala.microblogging.infrastructure.mongo;

import com.uala.microblogging.infrastructure.mongo.document.TweetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataTweetRepository extends MongoRepository<TweetDocument, UUID> {
}
