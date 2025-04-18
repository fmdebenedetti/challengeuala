package com.uala.microblogging.infrastructure.adapter;

import com.uala.microblogging.infrastructure.document.TweetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataTweetRepository extends MongoRepository<TweetDocument, UUID> {
}
