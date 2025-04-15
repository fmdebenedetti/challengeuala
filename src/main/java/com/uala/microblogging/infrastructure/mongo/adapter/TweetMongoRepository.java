package com.uala.microblogging.infrastructure.mongo.adapter;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.domain.repository.TweetRepository;
import com.uala.microblogging.infrastructure.mongo.SpringDataTweetRepository;
import com.uala.microblogging.infrastructure.mongo.document.TweetDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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
    public List<Tweet> findAllByIds(List<UUID> tweetIds) {
        return List.of();
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
