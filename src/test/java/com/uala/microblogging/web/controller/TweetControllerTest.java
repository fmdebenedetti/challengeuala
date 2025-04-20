package com.uala.microblogging.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.web.dto.request.PostTweetRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TweetController.class)
@Import(TweetControllerTest.TestConfig.class)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostTweetUseCase postTweetUseCase;

    static class TestConfig {
        @Bean
        PostTweetUseCase postTweetUseCase() {
            return Mockito.mock(PostTweetUseCase.class);
        }
    }

    @Test
    void shouldPostTweetSuccessfully() throws Exception {
        // given
        UUID userId    = UUID.randomUUID();
        String content = "¡Hola mundo desde el test!";
        UUID tweetId   = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().withNano(0);

        Tweet tweet = Tweet.builder()
                .id(tweetId)
                .userId(userId)
                .content(content)
                .createdAt(createdAt)
                .build();

        given(postTweetUseCase.postTweet(userId, content))
                .willReturn(tweet);

        PostTweetRequest req = new PostTweetRequest();
        req.setUserId(userId);
        req.setContent(content);

        // when & then
        mockMvc.perform(post("/api/tweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.tweets[0].id").value(tweetId.toString()))
                .andExpect(jsonPath("$.tweets[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$.tweets[0].content").value(content))
                .andExpect(jsonPath("$.tweets[0].createdAt").value(createdAt.toString()));
    }
}
