package com.uala.microblogging.infrastructure.repository.mongo;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.adapter.SpringDataTweetRepository;
import com.uala.microblogging.infrastructure.document.TweetDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TweetMongoRepository implements TweetRepository {

    private final SpringDataTweetRepository repository;

    @Override
    public Tweet save(Tweet tweet) {
        TweetDocument document = TweetDocument.builder()
                .id(tweet.getId())
                .userId(tweet.getUserId())
                .content(tweet.getContent())
                .createdAt(tweet.getCreatedAt())
                .build();
        return toDomain(repository.save(document));
    }

    @Override
    public List<Tweet> findByUserIdInOrderByCreatedAtDesc(List<UUID> userIds) {
        List<TweetDocument> documents = repository.findByUserIdInOrderByCreatedAtDesc(userIds);
        return documents.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Tweet toDomain(TweetDocument document) {
        return Tweet.builder()
                .id(document.getId())
                .userId(document.getUserId())
                .content(document.getContent())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
