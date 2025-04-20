package com.uala.microblogging.applicaction.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uala.microblogging.application.exception.RedisErrorException;
import com.uala.microblogging.application.service.GetTimelineService;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.FollowRepository;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.repository.redis.TimelineRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetTimelineServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private TimelineRedisRepository timelineRedisRepository;

    @InjectMocks
    private GetTimelineService getTimelineService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        getTimelineService = new GetTimelineService(followRepository, tweetRepository, timelineRedisRepository, objectMapper);
    }

    @Test
    void shouldReturnTweetsFromMongoAndCacheThemIfRedisEmpty() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();

        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .content("Desde Mongo")
                .createdAt(LocalDateTime.now())
                .build();

        when(timelineRedisRepository.getTimeline(userId)).thenReturn(Collections.emptyList());
        when(followRepository.findFollowerIdsByUserId(userId)).thenReturn(List.of(followeeId));
        when(tweetRepository.findByUserIdInOrderByCreatedAtDesc(List.of(followeeId))).thenReturn(List.of(tweet));

        List<Tweet> result = getTimelineService.getTimeline(userId);

        assertEquals(1, result.size());
        assertEquals(tweet.getId(), result.get(0).getId());

        verify(timelineRedisRepository).addTweetToTimeline(eq(userId), anyString());
    }

    @Test
    void shouldThrowExceptionIfRedisFailsToStore() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();

        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .content("Error al guardar")
                .createdAt(LocalDateTime.now())
                .build();

        when(timelineRedisRepository.getTimeline(userId)).thenReturn(Collections.emptyList());
        when(followRepository.findFollowerIdsByUserId(userId)).thenReturn(List.of(followeeId));
        when(tweetRepository.findByUserIdInOrderByCreatedAtDesc(List.of(followeeId))).thenReturn(List.of(tweet));
        // Forzar excepción al guardar en Redis
        lenient().doThrow(new RuntimeException("Redis error"))
                .when(timelineRedisRepository).addTweetToTimeline(any(), any());

        assertThrows(RedisErrorException.class, () -> getTimelineService.getTimeline(userId));
    }
}
