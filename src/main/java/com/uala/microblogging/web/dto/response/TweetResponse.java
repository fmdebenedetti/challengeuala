package com.uala.microblogging.web.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TweetResponse {
    private UUID userId;
    private List<TweetDTO> tweets;
}