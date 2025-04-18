package com.uala.microblogging.domain.repository;

import java.util.List;
import java.util.UUID;

public interface FollowRepository {
    List<String> findFollowerIdsByUserId(String userId);
    void follow(UUID followerId, UUID followeeId);
}