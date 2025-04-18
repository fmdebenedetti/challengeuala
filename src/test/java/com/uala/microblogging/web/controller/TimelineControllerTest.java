package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TimelineControllerTest {

    @Mock
    private GetTimelineUseCase getTimelineUseCase;

    @InjectMocks
    private TimelineController timelineController;

    private MockMvc mockMvc;

    @Test
    public void testGetTimeline() throws Exception {
        // given
        mockMvc = MockMvcBuilders.standaloneSetup(timelineController).build();

        UUID userId = UUID.randomUUID();
        LocalDateTime cursor = LocalDateTime.now();
        int limit = 20;

        Tweet tweet = Tweet.builder()
                .content("This is a test tweet")
                .createdAt(LocalDateTime.now())
                .build();

        when(getTimelineUseCase.getTimeline(userId, cursor, limit))
                .thenReturn(Collections.singletonList(tweet));

        // Assert
        mockMvc.perform(get("/api/timeline")
                        .header("X-USER-ID", userId.toString())
                        .param("cursor", cursor.toString())
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("This is a test tweet"));

        // Verify that the use case method was called once with the correct parameters
        verify(getTimelineUseCase).getTimeline(userId, cursor, limit);
    }

    @Test
    public void testGetTimelineWithoutCursor() throws Exception {
        // given
        mockMvc = MockMvcBuilders.standaloneSetup(timelineController).build();

        UUID userId = UUID.randomUUID();
        int limit = 20;

        Tweet tweet = Tweet.builder()
                .content("This is a test tweet")
                .createdAt(LocalDateTime.now())
                .build();

        when(getTimelineUseCase.getTimeline(userId, LocalDateTime.MIN, limit))
                .thenReturn(Collections.singletonList(tweet));

        // Assert
        mockMvc.perform(get("/api/timeline")
                        .header("X-USER-ID", userId.toString())
                        .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("This is a test tweet"));

        // Verify that the use case method was called once with the default cursor (LocalDateTime.MIN)
        verify(getTimelineUseCase).getTimeline(userId, LocalDateTime.MIN, limit);
    }
}
