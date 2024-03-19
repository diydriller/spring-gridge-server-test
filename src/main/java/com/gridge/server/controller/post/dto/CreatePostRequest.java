package com.gridge.server.controller.post.dto;

import com.gridge.server.service.post.dto.PostInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreatePostRequest {
    @Size(max = 100, message = "게시물 내용은 100자 이하로 입력해주세요")
    @NotBlank(message = "게시물 내용을 입력해주세요")
    private String content;
    @Size(min = 1, message = "게시물 이미지를 등록해주세요")
    @NotEmpty(message = "게시물 이미지를 등록해주세요")
    private List<MultipartFile> postImages = new ArrayList<>();

    public PostInfo toInfo() {
        return PostInfo.builder()
                .content(content)
                .build();
    }
}
