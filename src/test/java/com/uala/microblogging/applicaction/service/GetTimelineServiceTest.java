package com.uala.microblogging.applicaction.service;

import com.uala.microblogging.application.service.GetTimelineService;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TimelineRepository;
import com.uala.microblogging.domain.repository.TweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GetTimelineServiceTest {

    @Mock
    private TimelineRepository timelineRepository;

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private GetTimelineService getTimelineService;

    @Test
    public void testGetTimeline() {
        // Arrange
        UUID userId = UUID.randomUUID();
        LocalDateTime cursor = LocalDateTime.now().minusDays(1);
        int limit = 10;

        UUID tweetId1 = UUID.randomUUID();
        UUID tweetId2 = UUID.randomUUID();

        Tweet tweet1 = Tweet.builder()
                .id(tweetId1)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        Tweet tweet2 = Tweet.builder()
                .id(tweetId2)
                .createdAt(LocalDateTime.now().minusMinutes(30))
                .build();

        // Mocking the timelineRepository and tweetRepository
        when(timelineRepository.getTweetIdsForUser(userId, cursor, limit))
                .thenReturn(Arrays.asList(tweetId1, tweetId2));
        when(tweetRepository.findByUserIdInOrderByCreatedAtDesc(Arrays.asList(tweetId1, tweetId2)))
                .thenReturn(Arrays.asList(tweet1, tweet2));

        // Act
        List<Tweet> tweets = getTimelineService.getTimeline(userId);

        // Assert
        assertEquals(2, tweets.size());
        assertEquals(tweet2, tweets.get(0));  // El tweet más reciente debe ser el primero
        assertEquals(tweet1, tweets.get(1));  // El tweet más antiguo debe ser el segundo

        // Verificamos que los repositorios fueron llamados correctamente
        verify(timelineRepository).getTweetIdsForUser(userId, cursor, limit);
        verify(tweetRepository).findByUserIdInOrderByCreatedAtDesc(Arrays.asList(tweetId1, tweetId2));
    }

    @Test
    public void testGetTimelineNoTweets() {
        // Arrange
        UUID userId = UUID.randomUUID();
        LocalDateTime cursor = LocalDateTime.now().minusDays(1);
        int limit = 10;

        // Mocking the timelineRepository and tweetRepository for no tweets
        when(timelineRepository.getTweetIdsForUser(userId, cursor, limit))
                .thenReturn(Collections.emptyList());

        // Act
        List<Tweet> tweets = getTimelineService.getTimeline(userId);

        // Assert
        assertEquals(0, tweets.size());  // Debería devolver una lista vacía

        // Verificamos que los repositorios fueron llamados correctamente
        verify(timelineRepository).getTweetIdsForUser(userId, cursor, limit);
        verify(tweetRepository).findByUserIdInOrderByCreatedAtDesc(Collections.emptyList());
    }
}
