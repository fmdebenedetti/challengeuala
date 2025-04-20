package com.uala.microblogging.infrastructure.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "followers")
@Builder
public class FollowerDocument {
    @Id
    private UUID id;
    private List<UUID> followerIds;
}
