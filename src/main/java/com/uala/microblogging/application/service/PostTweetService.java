package com.uala.microblogging.application.service;

import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class PostTweetService implements PostTweetUseCase {

    private final TweetRepository tweetRepository;

    private static final int MAX_TWEET_LENGTH = 280;

    @Override
    public Tweet postTweet(UUID userId, String content) {
        if (content.length() > MAX_TWEET_LENGTH) {
            throw new IllegalArgumentException("Content must be less than or equal to 280 characters.");
        }
        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return tweetRepository.save(tweet);
    }
}
