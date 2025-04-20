package com.uala.microblogging.application.service;

import com.uala.microblogging.application.exception.SelfFollowException;
import com.uala.microblogging.application.useCase.FollowUserUseCase;
import com.uala.microblogging.infrastructure.repository.mongo.FollowMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowUserService implements FollowUserUseCase {

    private final FollowMongoRepository followMongoRepository;

    @Override
    public void follow(UUID followerId, UUID followeeId) {
        if (followerId.equals(followeeId)) {
            throw new SelfFollowException();
        }
        followMongoRepository.follow(followerId, followeeId);
    }
}
