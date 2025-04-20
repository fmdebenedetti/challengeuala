package com.uala.microblogging.domain.repository;

import com.uala.microblogging.domain.model.Tweet;

import java.util.List;
import java.util.UUID;

public interface TweetRepository {
    Tweet save(Tweet tweet);
    List<Tweet> findByUserIdInOrderByCreatedAtDesc(List<UUID> tweetIds);
}
