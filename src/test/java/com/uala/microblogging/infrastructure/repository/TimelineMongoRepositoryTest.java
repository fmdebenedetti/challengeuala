package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.infrastructure.document.TimelineDocument;
import com.uala.microblogging.infrastructure.repository.mongo.TimelineMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TimelineMongoRepositoryTest {

    private MongoRepository<TimelineDocument, String> mongoRepository;
    private TimelineMongoRepository repository;

    @BeforeEach
    void setUp() {
        mongoRepository = mock(MongoRepository.class);
        repository = new TimelineMongoRepository(mongoRepository);
    }

    @Test
    void shouldReturnEmptyList_WhenTimelineDocumentIsMissing() {
        UUID userId = UUID.randomUUID();
        LocalDateTime cursor = LocalDateTime.now();

        when(mongoRepository.findById(userId.toString())).thenReturn(Optional.empty());

        List<UUID> result = repository.getTweetIdsForUser(userId, cursor, 10);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnFilteredTweetIds_AfterCursorWithLimit() {
        UUID userId = UUID.randomUUID();
        LocalDateTime cursor = LocalDateTime.now().minusMinutes(10);

        Tweet tweet1 = new Tweet(UUID.randomUUID(), userId, "Tweet 1", cursor.plusMinutes(1));
        Tweet tweet2 = new Tweet(UUID.randomUUID(), userId, "Tweet 2", cursor.plusMinutes(2));
        Tweet tweetOld = new Tweet(UUID.randomUUID(), userId, "Old", cursor.minusMinutes(5)); // filtered out

        TimelineDocument timelineDocument = TimelineDocument.builder()
                .id(userId.toString())
                .tweets(List.of(tweet1, tweet2, tweetOld))
                .build();

        when(mongoRepository.findById(userId.toString())).thenReturn(Optional.of(timelineDocument));

        List<UUID> result = repository.getTweetIdsForUser(userId, cursor, 2);

        assertThat(result).containsExactly(tweet1.getId(), tweet2.getId());
    }

    @Test
    void shouldSaveTimelineCorrectly() {
        UUID userId = UUID.randomUUID();
        List<Tweet> tweets = List.of(
                new Tweet(UUID.randomUUID(), userId, "Tweet 1", LocalDateTime.now())
        );

        repository.saveTimeline(userId, tweets);

        verify(mongoRepository).save(argThat(document ->
                document.getId().equals(userId.toString()) &&
                        document.getTweets().equals(tweets)
        ));
    }
}
