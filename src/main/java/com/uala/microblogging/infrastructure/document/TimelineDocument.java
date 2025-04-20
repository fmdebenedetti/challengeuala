package com.uala.microblogging.infrastructure.document;

import com.uala.microblogging.domain.model.Tweet;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "timelines")
@Builder
public class TimelineDocument {
    @Id
    private String id;
    private List<Tweet> tweets;
}
