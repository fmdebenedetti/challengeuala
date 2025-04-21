package com.uala.microblogging.infrastructure.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uala.microblogging.domain.model.Tweet;
import com.uala.microblogging.infrastructure.repository.mongo.FollowMongoRepository;
import com.uala.microblogging.infrastructure.repository.redis.TimelineRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TweetEventConsumer {

    private final FollowMongoRepository followMongoRepository;
    private final TimelineRedisRepository timelineRedisRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "tweets", groupId = "timeline-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            Tweet tweet = objectMapper.readValue(record.value(), Tweet.class);
            log.info("Consuming tweet: {}", tweet);

            List<UUID> followers = followMongoRepository.findFollowerIdsByUserId(tweet.getUserId());
            String tweetJson = objectMapper.writeValueAsString(tweet);

            // Agregar el tweet al timeline de cada seguidor
            followers.forEach(followerId ->
                    timelineRedisRepository.addTweetToTimeline(followerId, tweetJson)
            );

        } catch (Exception e) {
            log.error("Error processing tweet message", e);
        }
    }
}
