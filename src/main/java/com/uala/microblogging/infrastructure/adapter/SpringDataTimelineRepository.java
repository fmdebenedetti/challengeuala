package com.uala.microblogging.infrastructure.adapter;

import com.uala.microblogging.infrastructure.document.TimelineDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTimelineRepository extends MongoRepository<TimelineDocument, String> {
}
