package com.uala.microblogging.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.application.useCase.PostTweetUseCase;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.web.dto.PostTweetRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TweetController.class)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostTweetUseCase postTweetUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldPostTweetSuccessfully() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        String content = "Hola Mundo esto es un TEST!";
        Tweet tweet = new Tweet(UUID.randomUUID(), userId, content, LocalDateTime.now());

        Mockito.when(postTweetUseCase.postTweet(any(), any())).thenReturn(tweet);

        PostTweetRequest request = new PostTweetRequest();
        request.setContent(content);

        // assert
        mockMvc.perform(post("/api/tweets")
                        .header("X-USER-ID", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.content").value(content));
    }
}
