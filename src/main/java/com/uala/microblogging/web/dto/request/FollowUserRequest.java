package com.uala.microblogging.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FollowUserRequest {

    @NotNull
    private UUID followerId;

    @NotNull
    private UUID followeeId;
}
