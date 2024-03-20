package com.gridge.server.controller.admin;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.common.response.BaseResponseState;
import com.gridge.server.service.member.MemberService;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.PostService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.gridge.server.common.response.BaseResponseState.SUCCESS;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final MemberService memberService;
    private final PostService postService;
    @GetMapping("/admin/member")
    public BaseResponse<?> getMembers(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "date") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
            @RequestParam(name = "state") @Pattern(regexp = "^(ACTIVATED|SUSPENDED|CANCELLED)$") String state,
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size,
            @RequestAttribute("member") Member member
    ) {
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(memberService.getMembers(name, nickname, date, state, pageRequest));
    }

    @GetMapping("/admin/member/{id}")
    public BaseResponse<?> getMember(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(memberService.getMember(id));
    }

    @PatchMapping("/admin/member/{id}/suspend")
    public BaseResponse<?> updateMember(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        memberService.suspendMember(id);
        return new BaseResponse<>(SUCCESS);
    }

    @GetMapping("/admin/post")
    public BaseResponse<?> getPosts(
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "date") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
            @RequestParam(name = "state") @Pattern(regexp = "^(DELETED|NOT_DELETED)$") String state,
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size,
            @RequestAttribute("member") Member member
    ){
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(postService.getPosts(nickname, date, state, pageRequest));
    }

    @GetMapping("/admin/post/{id}")
    public BaseResponse<?> getPost(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        return new BaseResponse<>(postService.getPost(id));
    }

    @DeleteMapping("/admin/post/{id}")
    public BaseResponse<?> deletePost(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        postService.deletePost(id);
        return new BaseResponse<>(SUCCESS);
    }
}
