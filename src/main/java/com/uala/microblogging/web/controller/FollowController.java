package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.FollowUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowUserUseCase followUserUseCase;

    @PostMapping
    public void follow(@RequestParam UUID followerId, @RequestParam UUID followeeId) {
        followUserUseCase.follow(followerId, followeeId);
    }
}