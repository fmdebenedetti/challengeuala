package com.uala.microblogging.application.useCase;

import com.uala.microblogging.domain.model.Tweet;

import java.util.UUID;

public interface PostTweetUseCase {
    Tweet postTweet(UUID userId, String content);
}
