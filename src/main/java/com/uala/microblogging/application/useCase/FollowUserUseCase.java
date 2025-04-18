package com.uala.microblogging.application.useCase;

import java.util.UUID;

public interface FollowUserUseCase {
    void follow(UUID followerId, UUID followeeId);
}