package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.web.dto.request.PostTweetRequest;
import com.uala.microblogging.web.dto.response.TweetDTO;
import com.uala.microblogging.web.dto.response.TweetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final PostTweetUseCase postTweetUseCase;

    @PostMapping
    public ResponseEntity<TweetResponse> postTweet(@Valid @RequestBody PostTweetRequest request) {
        Tweet tweet = postTweetUseCase.postTweet(request.getUserId(), request.getContent());

        TweetDTO tweetDTO = TweetDTO.builder()
                .userId(tweet.getUserId())
                .id(tweet.getId())
                .content(tweet.getContent())
                .createdAt(tweet.getCreatedAt())
                .build();

        TweetResponse response = TweetResponse.builder()
                .userId(tweet.getUserId())
                .tweets(List.of(tweetDTO))
                .build();

        return ResponseEntity.ok(response);
    }
}