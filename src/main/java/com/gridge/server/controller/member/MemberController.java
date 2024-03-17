package com.gridge.server.controller.member;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.controller.member.dto.CheckNicknameRequest;
import com.gridge.server.controller.member.dto.CreateMemberRequest;
import com.gridge.server.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.gridge.server.common.response.BaseResponseState.SUCCESS;

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

    @PostMapping("/check/nickname")
    public BaseResponse<?> checkNickname(
            @RequestBody @Valid CheckNicknameRequest request
    ) {
        memberService.checkNicknameExist(request.getNickname());
        return new BaseResponse<>(SUCCESS);
    }
}
