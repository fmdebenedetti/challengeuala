package com.uala.microblogging.infrastructure.repository.mongo;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TimelineRepository;
import com.uala.microblogging.infrastructure.document.TimelineDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TimelineMongoRepository implements TimelineRepository {

    private final MongoRepository<TimelineDocument, String> mongoRepository;

    @Override
    public List<UUID> getTweetIdsForUser(UUID userId, LocalDateTime cursor, int limit) {
        // Implementación para obtener los IDs de los tweets del timeline de un usuario
        TimelineDocument timelineDocument = mongoRepository.findById(userId.toString()).orElse(null);

        if (timelineDocument == null) {
            return List.of();
        }

        // Filtra los tweets con la fecha del cursor y obtiene solo los IDs
        return timelineDocument.getTweets().stream()
                .filter(tweet -> tweet.getCreatedAt().isAfter(cursor))
                .map(tweet -> tweet.getId())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void saveTimeline(UUID userId, List<Tweet> tweets) {
        TimelineDocument timelineDocument = TimelineDocument.builder()
                .id(userId.toString())
                .tweets(tweets)
                .build();
        mongoRepository.save(timelineDocument);
    }
}
