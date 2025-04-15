package com.uala.microblogging.infrastructure.mongo;

import com.uala.microblogging.infrastructure.mongo.document.TimelineDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataTimelineRepository extends MongoRepository<TimelineDocument, String> {}
