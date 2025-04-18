package com.uala.microblogging.infrastructure.document;

import com.uala.microblogging.domain.model.Tweet;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "timelines")
public class TimelineDocument {
    @Id
    private String id; // userId
    private List<Tweet> tweets;
}
