package com.gridge.server.service.post.dto;

import com.gridge.server.service.post.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CommentInfo {
    private Long id;
    private String content;
    private String createdAt;
    private String updatedAt;

    public static CommentInfo from(Comment comment) {
        return CommentInfo.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreateAt().toString())
                .updatedAt(comment.getUpdateAt().toString())
                .build();
    }
}
