package com.gridge.server.controller.post.dto;

import com.gridge.server.service.post.dto.PostInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePostRequest {
    @Size(max = 100, message = "게시물 내용은 100자 이하로 입력해주세요")
    @NotBlank(message = "게시물 내용을 입력해주세요")
    private String content;

    public PostInfo toInfo() {
        return PostInfo.builder()
                .content(content)
                .build();
    }
}
