package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.infrastructure.adapter.SpringDataTweetRepository;
import com.uala.microblogging.infrastructure.document.TweetDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TweetMongoRepositoryTest {

    private SpringDataTweetRepository springDataTweetRepository;
    private TweetMongoRepository tweetMongoRepository;

    @BeforeEach
    void setUp() {
        springDataTweetRepository = mock(SpringDataTweetRepository.class);
        tweetMongoRepository = new TweetMongoRepository(springDataTweetRepository);
    }

    @Test
    void shouldSaveTweetCorrectly() {
        // Arrange
        UUID tweetId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String content = "Hello Twitter!";
        LocalDateTime createdAt = LocalDateTime.now();

        Tweet tweet = Tweet.builder()
                .id(tweetId)
                .userId(userId)
                .content(content)
                .createdAt(createdAt)
                .build();

        TweetDocument savedDocument = TweetDocument.builder()
                .id(tweetId)
                .userId(userId)
                .content(content)
                .createdAt(createdAt)
                .build();

        when(springDataTweetRepository.save(any())).thenReturn(savedDocument);

        // Act
        Tweet result = tweetMongoRepository.save(tweet);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(tweetId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);

        verify(springDataTweetRepository).save(argThat(doc ->
                doc.getId().equals(tweetId) &&
                        doc.getUserId().equals(userId) &&
                        doc.getContent().equals(content) &&
                        doc.getCreatedAt().equals(createdAt)
        ));
    }
}
