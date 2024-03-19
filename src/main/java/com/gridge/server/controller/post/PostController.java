package com.gridge.server.controller.post;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.controller.post.dto.CreateCommentRequest;
import com.gridge.server.controller.post.dto.CreatePostRequest;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public BaseResponse<?> createPost(
            @Valid CreatePostRequest request,
            @RequestAttribute("member") Member member
    ) {
        var postInfo = postService.createPost(request.toInfo(), request.getPostImages(), member);
        return new BaseResponse<>(postInfo);
    }

    @GetMapping("/post")
    public BaseResponse<?> getPosts(
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("size") int size,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(postService.getPosts(pageIndex, size));
    }

    @PostMapping("/post/{postId}/comment")
    public BaseResponse<?> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CreateCommentRequest request,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(postService.createComment(postId, request.toInfo(), member));
    }

    @GetMapping("/post/{postId}/comment")
    public BaseResponse<?> getComments(
            @PathVariable("postId") Long postId,
            @RequestParam("pageIndex") int pageIndex,
            @RequestParam("size") int size
    ) {
        return new BaseResponse<>(postService.getComments(postId, pageIndex, size));
    }
}
