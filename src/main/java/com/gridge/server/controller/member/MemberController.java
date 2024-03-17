package com.gridge.server.controller.member;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member")
    public BaseResponse<?> createMember(
            @RequestBody @Valid CreateMemberRequest request
    ) {
        var memberInfo = request.toInfo();
        memberService.createMember(memberInfo);
        return new BaseResponse<>(memberInfo);
    }
}
