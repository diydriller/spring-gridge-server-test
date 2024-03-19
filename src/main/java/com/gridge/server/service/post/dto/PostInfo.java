package com.gridge.server.service.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostInfo {
    private Long id;
    private String content;
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();
}
