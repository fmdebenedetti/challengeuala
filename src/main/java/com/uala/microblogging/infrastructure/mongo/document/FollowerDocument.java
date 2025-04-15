package com.uala.microblogging.infrastructure.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "followers")
public class FollowerDocument {
    @Id
    private String id;
    private List<String> followerIds;
}
