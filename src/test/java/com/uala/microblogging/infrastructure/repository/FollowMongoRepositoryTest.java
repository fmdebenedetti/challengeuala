package com.uala.microblogging.infrastructure.repository;

import com.uala.microblogging.infrastructure.document.FollowerDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class FollowMongoRepositoryTest {

    private MongoTemplate mongoTemplate;
    private FollowMongoRepository followMongoRepository;

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(MongoTemplate.class);
        followMongoRepository = new FollowMongoRepository(mongoTemplate);
    }

    @Test
    void shouldReturnFollowerIds_WhenDocumentExists() {
        String userId = UUID.randomUUID().toString();
        FollowerDocument doc = new FollowerDocument();
        doc.setId(userId);
        doc.setFollowerIds(List.of("follower1", "follower2"));

        when(mongoTemplate.findById(userId, FollowerDocument.class)).thenReturn(doc);

        List<String> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).containsExactly("follower1", "follower2");
    }

    @Test
    void shouldReturnEmptyList_WhenDocumentNotFound() {
        String userId = UUID.randomUUID().toString();

        when(mongoTemplate.findById(userId, FollowerDocument.class)).thenReturn(null);

        List<String> result = followMongoRepository.findFollowerIdsByUserId(userId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCreateNewFollowerDocument_IfNoneExists() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        when(mongoTemplate.findById(secondFollowerId.toString(), FollowerDocument.class)).thenReturn(null);

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(mongoTemplate).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId.toString()) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId.toString())
        ));
    }

    @Test
    void shouldAppendFollower_WhenNotAlreadyPresent() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        FollowerDocument existingDoc = new FollowerDocument();
        existingDoc.setId(secondFollowerId.toString());
        existingDoc.setFollowerIds(new ArrayList<>(List.of("other-follower")));

        when(mongoTemplate.findById(secondFollowerId.toString(), FollowerDocument.class)).thenReturn(existingDoc);

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(mongoTemplate).save(argThat(doc ->
                ((FollowerDocument) doc).getId().equals(secondFollowerId.toString()) &&
                        ((FollowerDocument) doc).getFollowerIds().contains(firsFollowerId.toString())
        ));
    }

    @Test
    void shouldNotAddFollower_IfAlreadyPresent() {
        UUID firsFollowerId = UUID.randomUUID();
        UUID secondFollowerId = UUID.randomUUID();

        FollowerDocument existingDoc = new FollowerDocument();
        existingDoc.setId(secondFollowerId.toString());
        existingDoc.setFollowerIds(new ArrayList<>(List.of(firsFollowerId.toString())));

        when(mongoTemplate.findById(secondFollowerId.toString(), FollowerDocument.class)).thenReturn(existingDoc);

        followMongoRepository.follow(firsFollowerId, secondFollowerId);

        verify(mongoTemplate).save(existingDoc); // still calls save, but doesn't duplicate
        assertThat(existingDoc.getFollowerIds()).containsOnlyOnce(firsFollowerId.toString());
    }
}