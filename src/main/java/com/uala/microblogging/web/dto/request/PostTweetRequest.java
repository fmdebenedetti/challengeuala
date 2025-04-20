package com.uala.microblogging.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PostTweetRequest {
    @NotNull
    private UUID userId;

    @NotBlank
    private String content;
}