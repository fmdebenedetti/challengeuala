package com.uala.microblogging.application.service;

import com.uala.microblogging.application.exception.TweetTooLongException;
import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.messaging.producer.TweetEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostTweetService implements PostTweetUseCase {

    private final TweetRepository tweetRepository;
    private final TweetEventProducer tweetEventProducer;

    private static final int MAX_TWEET_LENGTH = 280;

    @Override
    public Tweet postTweet(UUID userId, String content) {
        if (content.length() > MAX_TWEET_LENGTH) {
            throw new TweetTooLongException();
        }
        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        tweetRepository.save(tweet);
        tweetEventProducer.sendTweetPostedEvent(tweet);

        return tweet;
    }
}
