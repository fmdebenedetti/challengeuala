package com.uala.microblogging.application.exception;

public class RedisErrorException extends BusinessException {
    public RedisErrorException(String error) {
        super(error);
    }
}