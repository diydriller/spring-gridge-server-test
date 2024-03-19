package com.gridge.server.controller.post;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.controller.post.dto.CreatePostRequest;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public BaseResponse<?> createPost(
            @Valid CreatePostRequest request, @RequestAttribute("member") Member member) {
        var postInfo = postService.createPost(request.toInfo(), request.getPostImages(), member);
        return new BaseResponse<>(postInfo);
    }
}
