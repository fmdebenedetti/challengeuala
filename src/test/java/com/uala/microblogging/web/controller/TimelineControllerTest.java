package com.uala.microblogging.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TimelineController.class)
@Import(TimelineControllerTest.TestConfig.class)
class TimelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GetTimelineUseCase getTimelineUseCase;

    static class TestConfig {
        @Bean
        GetTimelineUseCase getTimelineUseCase() {
            return Mockito.mock(GetTimelineUseCase.class);
        }
    }

    @Test
    void shouldGetTimelineSuccessfully() throws Exception {
        // given
        UUID userId = UUID.randomUUID();

        Tweet tweet1 = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content("First tweet")
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        Tweet tweet2 = Tweet.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .content("Second tweet")
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        given(getTimelineUseCase.getTimeline(userId))
                .willReturn(List.of(tweet1, tweet2));

        // --- Act & Assert ---
        mockMvc.perform(get("/api/timeline/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.tweets.length()").value(2))
                // Validamos el primer tweet
                .andExpect(jsonPath("$.tweets[0].id").value(tweet1.getId().toString()))
                .andExpect(jsonPath("$.tweets[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$.tweets[0].content").value("First tweet"))
                .andExpect(jsonPath("$.tweets[0].createdAt").value(tweet1.getCreatedAt().toString()))
                // Validamos el segundo tweet
                .andExpect(jsonPath("$.tweets[1].id").value(tweet2.getId().toString()))
                .andExpect(jsonPath("$.tweets[1].userId").value(userId.toString()))
                .andExpect(jsonPath("$.tweets[1].content").value("Second tweet"))
                .andExpect(jsonPath("$.tweets[1].createdAt").value(tweet2.getCreatedAt().toString()));
    }
}
