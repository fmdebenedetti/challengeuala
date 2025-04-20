package com.uala.microblogging.infrastructure.repository.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TimelineRedisRepositoryImpl implements TimelineRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final int MAX_TWEETS = 100;

    @Override
    public void addTweetToTimeline(UUID userId, String tweetJson) {

        String key = "timeline:" + userId;
        redisTemplate.opsForList().leftPush(key, tweetJson);
        redisTemplate.opsForList().trim(key, 0, MAX_TWEETS - 1);
    }

    @Override
    public List<String> getTimeline(UUID userId) {
        String key = "timeline:" + userId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
