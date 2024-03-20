package com.gridge.server.service.post.dto;

import com.gridge.server.service.post.entity.Post;
import com.gridge.server.service.post.entity.PostImage;
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
    private String creatAt;
    private String updateAt;

    public static PostInfo from(Post post) {
        return PostInfo.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrls(post.getPostImages().stream()
                        .map(PostImage::getImageUrl)
                        .toList())
                .creatAt(post.getCreateAt().toString())
                .updateAt(post.getUpdateAt().toString())
                .build();
    }
}
