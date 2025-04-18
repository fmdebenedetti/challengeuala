package com.uala.microblogging.application.service;

import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TimelineRepository;
import com.uala.microblogging.domain.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTimelineService implements GetTimelineUseCase {

    private final TimelineRepository timelineRepository;
    private final TweetRepository tweetRepository;

    @Override
    public List<Tweet> getTimeline(UUID userId, LocalDateTime cursor, int limit) {
        List<UUID> tweetIds = timelineRepository.getTweetIdsForUser(userId, cursor, limit);

        List<Tweet> tweets = tweetRepository.findAllByIds(tweetIds);

        return tweets.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
