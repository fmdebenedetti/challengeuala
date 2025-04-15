package com.uala.microblogging.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TweetResponse {
    private UUID id;
    private UUID userId;
    private String content;
    private LocalDateTime createdAt;
}