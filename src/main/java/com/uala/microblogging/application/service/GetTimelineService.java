package com.uala.microblogging.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.application.exception.RedisErrorException;
import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.FollowRepository;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.repository.redis.TimelineRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTimelineService implements GetTimelineUseCase {

    private final FollowRepository followRepository;
    private final TweetRepository tweetRepository;
    private final TimelineRedisRepository timelineRedisRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<Tweet> getTimeline(UUID userId) {
       List<String> cachedTimeline = timelineRedisRepository.getTimeline(userId);

        if (cachedTimeline != null && !cachedTimeline.isEmpty()) {
            return cachedTimeline.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, Tweet.class);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        List<UUID> followings = followRepository.findFollowerIdsByUserId(userId);
        List<Tweet> tweets = tweetRepository.findByUserIdInOrderByCreatedAtDesc(followings);

        tweets.forEach(tweet -> {
            try {
                String json = objectMapper.writeValueAsString(tweet);
                timelineRedisRepository.addTweetToTimeline(userId, json);
            } catch (Exception e) {
                throw new RedisErrorException("Error al refrescar Redis de Tweets");
            }
        });

        return tweets;
    }
}
