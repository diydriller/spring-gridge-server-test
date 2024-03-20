package com.gridge.server.service.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostReportInfo {
    private Long id;
    private String content;
    private String nickname;
    private String createAt;
    private String updateAt;
}
