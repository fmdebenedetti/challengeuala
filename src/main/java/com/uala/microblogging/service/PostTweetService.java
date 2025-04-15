package com.uala.microblogging.service;

import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class PostTweetService implements PostTweetUseCase {

    private final TweetRepository tweetRepository;

    @Override
    public Tweet execute(UUID userId, String content) {
        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        return tweetRepository.save(tweet);
    }
}
