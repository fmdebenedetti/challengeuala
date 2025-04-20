package com.uala.microblogging.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.application.useCase.FollowUserUseCase;
import com.uala.microblogging.web.dto.request.FollowUserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowController.class)
@Import(FollowControllerTest.TestConfig.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FollowUserUseCase followUserUseCase;

    static class TestConfig {
        @Bean
        FollowUserUseCase followUserUseCase() {
            return Mockito.mock(FollowUserUseCase.class);
        }
    }

    @Test
    void shouldFollowUserSuccessfully() throws Exception {
        // given
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();
        FollowUserRequest req = new FollowUserRequest();
        req.setFollowerId(followerId);
        req.setFolloweeId(followeeId);

        // Act & Assert
        mockMvc.perform(post("/api/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(followUserUseCase).follow(followerId, followeeId);
    }
}
