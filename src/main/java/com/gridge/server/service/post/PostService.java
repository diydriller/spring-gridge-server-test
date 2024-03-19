package com.gridge.server.service.post;

import com.gridge.server.dataManager.post.PostRepository;
import com.gridge.server.service.common.FileService;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.dto.PostInfo;
import com.gridge.server.service.post.entity.Post;
import com.gridge.server.service.post.entity.PostImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;

    @Transactional
    public PostInfo createPost(PostInfo info, List<MultipartFile> files, Member member) {
        List<PostImage> postImages = files.stream()
                .map(file -> {
                    try {
                        fileService.checkImageType(file);
                        String imageUrl = fileService.saveFile(file);
                        info.getImageUrls().add(imageUrl);
                        return PostImage.builder()
                                .imageUrl(imageUrl)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        var post = Post.builder()
                .content(info.getContent())
                .build();
        post.setPostImages(postImages);
        post.setMember(member);
        postRepository.save(post);

        info.setId(post.getId());
        return info;
    }
}
