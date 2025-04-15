package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.GetTimelineUseCase;
import com.uala.microblogging.domain.model.Tweet;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final GetTimelineUseCase getTimelineUseCase;

    @GetMapping
    public List<Tweet> getTimeline(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        LocalDateTime effectiveCursor = (cursor != null) ? cursor : LocalDateTime.MIN;
        return getTimelineUseCase.getTimeline(userId, effectiveCursor, limit);
    }
}