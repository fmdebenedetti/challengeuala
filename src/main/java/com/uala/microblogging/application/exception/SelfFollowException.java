package com.uala.microblogging.application.exception;

public class SelfFollowException extends BusinessException {
    public SelfFollowException() {
        super("A user cannot follow themselves.");
    }
}
