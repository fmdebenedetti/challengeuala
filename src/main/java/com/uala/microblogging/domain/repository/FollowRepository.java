package com.uala.microblogging.domain.repository;

import java.util.List;
import java.util.UUID;

public interface FollowRepository {
    void follow(UUID followerId, UUID followeeId);
    List<UUID> findFollowerIdsByUserId(UUID followerId);
}