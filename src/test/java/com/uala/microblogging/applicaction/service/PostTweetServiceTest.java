package com.uala.microblogging.applicaction.service;

import com.uala.microblogging.application.exception.TweetTooLongException;
import com.uala.microblogging.application.service.PostTweetService;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.messaging.producer.TweetEventProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostTweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private PostTweetService postTweetService;

    @Mock
    private TweetEventProducer tweetEventProducer;

    @Test
    public void testPostTweetSuccessful() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String content = "This is a valid tweet content.";
        Tweet tweet = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        doReturn(tweet).when(tweetRepository).save(any(Tweet.class));

        // Act
        Tweet result = postTweetService.postTweet(userId, content);

        // Assert
        assertNotNull(result);
        assertEquals(content, result.getContent());
        verify(tweetRepository).save(any(Tweet.class));
    }

    @Test
    public void testPostTweetThrowsExceptionWhenContentExceedsMaxLength() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String longContent = "a".repeat(281);

        // Act & Assert
        TweetTooLongException exception = assertThrows(TweetTooLongException.class, () -> {
            postTweetService.postTweet(userId, longContent);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Tweet exceeds the 280 character limit.", exception.getMessage());
    }
}
