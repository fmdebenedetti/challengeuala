package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.infrastructure.document.FollowerDocument;
import com.uala.microblogging.infrastructure.document.TimelineDocument;
import com.uala.microblogging.infrastructure.repository.mongo.FollowMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class FollowMongoRepositoryTest {

    private MongoRepository<FollowerDocument, UUID> repository;
    private FollowMongoRepository followMongoRepository;

    @BeforeEach
    void setUp() {
        repository = mock(MongoRepository.class);
        followMongoRepository = new FollowMongoRepository(repository);
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

        when(repository.findById(userId)).thenReturn(Optional.ofNullable(doc));

        List<UUID> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).containsExactly(follower1, follower2);
    }

    @Test
    void shouldReturnEmptyList_WhenDocumentNotFound() {
        UUID userId = UUID.randomUUID();

        when(repository.findById(userId)).thenReturn(Optional.empty());

        List<UUID> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateNewFollowerDocument_IfNoneExists() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        when(repository.findById(secondFollowerId)).thenReturn(Optional.empty());

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(repository).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId)
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

        when(repository.findById(any())).thenReturn(Optional.ofNullable(existingDoc));

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(repository).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId)
        ));
    }

    @Test
    void shouldNotAddFollower_IfAlreadyPresent() {
        UUID firstFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        FollowerDocument existingDoc = FollowerDocument.builder()
                .id(secondFollowerId)
                .followerIds(new ArrayList<>(List.of(firstFollowerId)))
                .build();

        when(repository.findById(secondFollowerId)).thenReturn(Optional.of(existingDoc));

        followMongoRepository.follow(firstFollowerId, secondFollowerId);

        verify(repository, never()).save(any(FollowerDocument.class));

        assertThat(existingDoc.getFollowerIds()).containsOnlyOnce(firstFollowerId);
    }

}