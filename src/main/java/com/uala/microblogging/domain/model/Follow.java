package com.uala.microblogging.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Follow {
    private UUID followerId;
    private UUID followedId;
}
