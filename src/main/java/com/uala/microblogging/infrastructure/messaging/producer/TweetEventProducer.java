package com.uala.microblogging.infrastructure.messaging.producer;

import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.infrastructure.messaging.event.TweetPostedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TweetEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "tweet.posted";

    public void sendTweetPostedEvent(Tweet tweet) {
        TweetPostedEvent event = TweetPostedEvent.builder()
                .id(tweet.getId())
                .userId(tweet.getUserId())
                .content(tweet.getContent())
                .createdAt(tweet.getCreatedAt())
                .build();

        kafkaTemplate.send(TOPIC, tweet.getId().toString(), event);
    }
}
