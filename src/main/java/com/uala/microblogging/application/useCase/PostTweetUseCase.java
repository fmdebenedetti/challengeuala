package com.uala.microblogging.application.useCase;

import com.uala.microblogging.domain.model.Tweet;

import java.util.UUID;

public interface PostTweetUseCase {
    Tweet execute(UUID userId, String content);
}
