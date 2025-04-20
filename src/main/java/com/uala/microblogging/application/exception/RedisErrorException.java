package com.uala.microblogging.application.exception;

public class RedisErrorException extends BusinessException {
    public RedisErrorException() {
        super("Redis error.");
    }
    public RedisErrorException(String error) {
        super(error);
    }
}