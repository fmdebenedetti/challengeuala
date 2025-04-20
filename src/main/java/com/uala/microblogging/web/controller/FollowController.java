package com.uala.microblogging.web.controller;

import com.uala.microblogging.application.useCase.FollowUserUseCase;
import com.uala.microblogging.web.dto.request.FollowUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowUserUseCase followUserUseCase;

    @PostMapping
    public ResponseEntity<Void> follow(@Valid @RequestBody FollowUserRequest request) {
        followUserUseCase.follow(request.getFollowerId(), request.getFolloweeId());
        return ResponseEntity.ok().build();
    }
}