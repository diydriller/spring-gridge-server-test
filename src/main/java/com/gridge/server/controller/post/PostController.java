package com.gridge.server.controller.post;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.controller.post.dto.CommentRequest;
import com.gridge.server.controller.post.dto.CreatePostReportRequest;
import com.gridge.server.controller.post.dto.CreatePostRequest;
import com.gridge.server.controller.post.dto.UpdatePostRequest;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gridge.server.common.response.BaseResponseState.SUCCESS;


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

    @PutMapping("/post/{postId}")
    public BaseResponse<?> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid UpdatePostRequest request,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(postService.updatePost(postId, request.toInfo(), member));
    }

    @DeleteMapping("/post/{postId}")
    public BaseResponse<?> deletePost(
            @PathVariable("postId") Long postId,
            @RequestAttribute("member") Member member
    ) {
        postService.deletePost(postId, member);
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/post/{postId}/comment")
    public BaseResponse<?> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentRequest request,
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

    @PutMapping("/post/{postId}/comment/{commentId}")
    public BaseResponse<?> updateComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid CommentRequest request,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(postService.updateComment(postId, commentId, request.toInfo(), member));
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public BaseResponse<?> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestAttribute("member") Member member
    ) {
        postService.deleteComment(postId, commentId, member);
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/post/{postId}/report")
    public BaseResponse<?> reportPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CreatePostReportRequest request,
            @RequestAttribute("member") Member member
    ) {
        postService.reportPost(postId, request.toInfo(), member);
        return new BaseResponse<>(SUCCESS);
    }
}
