package com.gridge.server.controller.post.dto;

import com.gridge.server.service.post.dto.PostReportInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostReportRequest {
    @Size(max = 100, message = "신고 내용은 100자 이하로 입력해주세요")
    @NotBlank(message = "신고 내용을 입력해주세요")
    private String content;

    public PostReportInfo toInfo(){
        return PostReportInfo.builder()
                .content(content)
                .build();
    }
}
