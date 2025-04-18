package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.FollowUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FollowControllerTest {

    private FollowUserUseCase followUserUseCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        followUserUseCase = Mockito.mock(FollowUserUseCase.class);
        FollowController controller = new FollowController(followUserUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldFollowUserSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();

        // No need to stub followUserUseCase.follow() since it's void
        mockMvc.perform(post("/api/follow")
                        .param("followerId", userId.toString())
                        .param("followeeId", targetUserId.toString()))
                .andExpect(status().isOk());

        verify(followUserUseCase, times(1)).follow(userId, targetUserId);
    }
}
