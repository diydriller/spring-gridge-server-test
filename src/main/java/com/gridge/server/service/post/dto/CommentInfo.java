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

    public static CommentInfo from(Comment comment) {
        return CommentInfo.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
