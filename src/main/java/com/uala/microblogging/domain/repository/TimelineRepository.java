package com.uala.microblogging.domain.repository;


import com.uala.microblogging.domain.model.Tweet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TimelineRepository {
    List<UUID> getTweetIdsForUser(UUID userId, LocalDateTime cursor, int limit);
    void saveTimeline(UUID userId, List<Tweet> tweets);
}