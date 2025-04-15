package com.uala.microblogging.domain.repository;

import java.util.List;

public interface FollowRepository {
    List<String> findFollowerIdsByUserId(String userId);
}