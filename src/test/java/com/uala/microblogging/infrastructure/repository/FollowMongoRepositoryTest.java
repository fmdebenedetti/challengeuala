package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.infrastructure.adapter.SpringDataFollowRepository;
import com.uala.microblogging.infrastructure.document.FollowerDocument;
import com.uala.microblogging.infrastructure.repository.mongo.FollowMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class FollowMongoRepositoryTest {

    private SpringDataFollowRepository springDataFollowRepository;
    private FollowMongoRepository followMongoRepository;

    @BeforeEach
    void setUp() {
        springDataFollowRepository = mock(SpringDataFollowRepository.class);
        followMongoRepository = new FollowMongoRepository(springDataFollowRepository);
    }

    @Test
    void shouldReturnFollowerIds_WhenDocumentExists() {
        UUID userId = UUID.randomUUID();
        UUID follower1 = UUID.randomUUID();
        UUID follower2 = UUID.randomUUID();
        FollowerDocument doc = FollowerDocument.builder()
                .id(userId)
                .followerIds(List.of(follower1, follower2))
                .build();

        when(springDataFollowRepository.findById(userId)).thenReturn(Optional.ofNullable(doc));

        List<UUID> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).containsExactly(follower1, follower2);
    }

    @Test
    void shouldReturnEmptyList_WhenDocumentNotFound() {
        UUID userId = UUID.randomUUID();

        when(springDataFollowRepository.findById(userId)).thenReturn(null);

        List<UUID> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateNewFollowerDocument_IfNoneExists() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        when(springDataFollowRepository.findById(secondFollowerId)).thenReturn(null);

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(springDataFollowRepository).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId.toString()) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId.toString())
        ));
    }

    @Test
    void shouldAppendFollower_WhenNotAlreadyPresent() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();
        UUID otherFollower = UUID.randomUUID();

        FollowerDocument existingDoc = FollowerDocument.builder()
                .id(secondFollowerId)
                .followerIds(List.of(otherFollower))
                .build();

        when(springDataFollowRepository.findById(secondFollowerId)).thenReturn(Optional.ofNullable(existingDoc));

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(springDataFollowRepository).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId.toString()) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId.toString())
        ));
    }

    @Test
    void shouldNotAddFollower_IfAlreadyPresent() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        FollowerDocument existingDoc = FollowerDocument.builder()
                .id(secondFollowerId)
                .followerIds(List.of(firsFollowerId))
                .build();

        when(springDataFollowRepository.findById(secondFollowerId)).thenReturn(Optional.ofNullable(existingDoc));

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(springDataFollowRepository).save(existingDoc); // still calls save, but doesn't duplicate
        assertThat(existingDoc.getFollowerIds()).containsOnlyOnce(firsFollowerId);
    }
}