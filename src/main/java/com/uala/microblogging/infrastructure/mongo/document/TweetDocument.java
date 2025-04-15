package com.uala.microblogging.infrastructure.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "tweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetDocument {
    @Id
    private UUID id;
    private UUID userId;
    private String content;
    private LocalDateTime createdAt;
}
