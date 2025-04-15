package com.uala.microblogging.application.useCase;

import com.uala.microblogging.domain.model.Tweet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetTimelineUseCase {
    List<Tweet> getTimeline(UUID userId, LocalDateTime cursor, int limit);
}