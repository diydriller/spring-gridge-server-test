package com.gridge.server.controller.post.dto;

import com.gridge.server.service.post.dto.CommentInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {
    @Size(max = 100, message = "댓글 내용은 100자 이하로 입력해주세요")
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    public CommentInfo toInfo(){
        return CommentInfo.builder()
                .content(content)
                .build();
    }
}
