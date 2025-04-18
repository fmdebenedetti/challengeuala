package com.uala.microblogging.domain.exception;

import java.util.UUID;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException(UUID id) {
        super("Tweet with ID " + id + " not found");
    }
}
