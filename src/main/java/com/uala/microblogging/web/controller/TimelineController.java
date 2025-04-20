package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.web.dto.response.TimelineResponse;
import com.uala.microblogging.web.dto.response.TweetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final GetTimelineUseCase getTimelineUseCase;

    @GetMapping("/{userId}")
    public ResponseEntity<TimelineResponse> getTimeline(@PathVariable UUID userId) {
        List<Tweet> tweets = getTimelineUseCase.getTimeline(userId);

        List<TweetDTO> tweetDTOs = tweets.stream()
                .map(tweet -> TweetDTO.builder()
                        .id(tweet.getId())
                        .userId(tweet.getUserId())
                        .content(tweet.getContent())
                        .createdAt(tweet.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        TimelineResponse response = TimelineResponse.builder()
                .userId(userId)
                .tweets(tweetDTOs)
                .build();

        return ResponseEntity.ok(response);
    }
}