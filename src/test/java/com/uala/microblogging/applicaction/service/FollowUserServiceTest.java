package com.uala.microblogging.applicaction.service;

import com.uala.microblogging.application.service.FollowUserService;
import com.uala.microblogging.infrastructure.repository.FollowMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FollowUserServiceTest {

    @Mock
    private FollowMongoRepository followMongoRepository;

    @InjectMocks
    private FollowUserService followUserService;

    @Test
    public void testFollowSuccessful() {
        // given
        UUID followerId = UUID.randomUUID();
        UUID followeeId = UUID.randomUUID();

        // act
        followUserService.follow(followerId, followeeId);

        // assert
        verify(followMongoRepository, times(1)).follow(followerId, followeeId);
    }

    @Test
    public void testFollowThrowsExceptionWhenUserFollowsThemselves() {
        // given
        UUID userId = UUID.randomUUID();

        // act & assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            followUserService.follow(userId, userId);
        });

        // verificar que el mensaje de la excepción sea el esperado
        assertEquals("A user cannot follow themselves.", exception.getMessage());
    }
}
