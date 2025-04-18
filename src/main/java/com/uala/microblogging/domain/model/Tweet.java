package com.uala.microblogging.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Tweet {
    private UUID id;
    private UUID userId;
    private String content;
    private LocalDateTime createdAt;
}
