package com.uala.microblogging.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetPostedEvent {
    private UUID id;
    private UUID userId;
    private String content;
    private LocalDateTime createdAt;
}
