package com.uala.microblogging.infrastructure.repository.redis;

import java.util.List;
import java.util.UUID;

public interface TimelineRedisRepository {
    void addTweetToTimeline(UUID userId, String tweetJson);
    List<String> getTimeline(UUID userId);
}
