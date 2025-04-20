package com.uala.microblogging.application.exception;

public class TweetTooLongException extends BusinessException {
    public TweetTooLongException() {
        super("Tweet exceeds the 280 character limit.");
    }
}

