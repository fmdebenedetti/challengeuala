package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.web.dto.PostTweetRequest;
import com.uala.microblogging.web.dto.TweetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private PostTweetUseCase postTweetUseCase;

    @PostMapping
    public ResponseEntity<TweetResponse> postTweet(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody PostTweetRequest request
    ) {
        Tweet tweet = postTweetUseCase.execute(userId, request.getContent());
        return ResponseEntity.ok(
                TweetResponse.builder()
                        .id(tweet.getId())
                        .userId(tweet.getUserId())
                        .content(tweet.getContent())
                        .createdAt(tweet.getCreatedAt())
                        .build()
        );
    }
}