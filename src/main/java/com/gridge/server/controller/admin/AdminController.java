package com.gridge.server.controller.admin;

import com.gridge.server.common.exception.AuthorizationException;
import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.service.member.MemberService;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.PostService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.gridge.server.common.response.BaseResponseState.AUTHORIZATION_ERROR;
import static com.gridge.server.common.response.BaseResponseState.SUCCESS;
import static com.gridge.server.service.member.entity.MemberType.ADMIN;

@RequiredArgsConstructor
@RestController
public class AdminController {
    private final MemberService memberService;
    private final PostService postService;
    @GetMapping("/admin/member")
    public BaseResponse<?> getMembersByAdmin(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "date") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
            @RequestParam(name = "state") @Pattern(regexp = "^(ACTIVATED|SUSPENDED|CANCELLED)$") String state,
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(memberService.getMembersByAdmin(name, nickname, date, state, pageRequest, member));
    }

    @GetMapping("/admin/member/{id}")
    public BaseResponse<?> getMemberByAdmin(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        return new BaseResponse<>(memberService.getMemberByAdmin(id, member));
    }

    @PatchMapping("/admin/member/{id}/suspend")
    public BaseResponse<?> suspendMemberByAdmin(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        memberService.suspendMemberByAdmin(id, member);
        return new BaseResponse<>(SUCCESS);
    }

    @GetMapping("/admin/post")
    public BaseResponse<?> getPostsByAdmin(
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "date") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
            @RequestParam(name = "state") @Pattern(regexp = "^(DELETED|NOT_DELETED)$") String state,
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size,
            @RequestAttribute("member") Member member
    ){
        checkAdmin(member);
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(postService.getPostsByAdmin(nickname, date, state, pageRequest, member));
    }

    @GetMapping("/admin/post/{id}")
    public BaseResponse<?> getPostByAdmin(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        return new BaseResponse<>(postService.getPostByAdmin(id, member));
    }

    @DeleteMapping("/admin/post/{id}")
    public BaseResponse<?> deletePostByAdmin(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        postService.deletePostByAdmin(id, member);
        return new BaseResponse<>(SUCCESS);
    }

    @GetMapping("/admin/report")
    public BaseResponse<?> getReportsByAdmin(
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size,
            @RequestAttribute("member") Member member
    ){
        checkAdmin(member);
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(postService.getReportsByAdmin(pageRequest, member));
    }

    @DeleteMapping("/admin/report/{id}")
    public BaseResponse<?> deleteReportByAdmin(
            @PathVariable(name = "id") long id,
            @RequestAttribute("member") Member member
    ) {
        checkAdmin(member);
        postService.deleteReportByAdmin(id, member);
        return new BaseResponse<>(SUCCESS);
    }

    private void checkAdmin(Member member) {
        if (member.getType() != ADMIN) {
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
    }
}
