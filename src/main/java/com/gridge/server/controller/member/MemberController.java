package com.gridge.server.controller.member;

import com.gridge.server.common.response.BaseResponse;
import com.gridge.server.controller.member.dto.CheckNicknameRequest;
import com.gridge.server.controller.member.dto.KakaoMemberRequest;
import com.gridge.server.controller.member.dto.CreateMemberRequest;
import com.gridge.server.controller.member.dto.LoginRequest;
import com.gridge.server.service.common.TokenService;
import com.gridge.server.service.member.MemberService;
import com.gridge.server.service.sns.KakaoRestClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static com.gridge.server.common.response.BaseResponseState.SUCCESS;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final KakaoRestClientService kakaoRestClientService;
    private final TokenService tokenService;

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

    @GetMapping("/kakao/callback")
    public BaseResponse<?> kakaoCallback(
            @RequestParam(value = "code") String code
    ) {
        return new BaseResponse<>(kakaoRestClientService.getAccessToken(code));
    }

    @PostMapping("/kakao/member")
    public BaseResponse<?> createKakaoMember(
            @RequestBody @Valid KakaoMemberRequest request
    ) {
        var memberInfo = memberService.createKakaoMember(request.getAccessToken());
        return new BaseResponse<>(memberInfo);
    }

    @PostMapping("/member/login")
    public BaseResponse<?> login(
            @RequestBody @Valid LoginRequest request
    ) {
        var memberInfo = request.toInfo();
        var member = memberService.login(memberInfo);
        return new BaseResponse<>(tokenService.createToken(member.getId()));
    }

    @PostMapping("/kakao/member/login")
    public BaseResponse<?> kakaoLogin(
            @RequestBody @Valid KakaoMemberRequest request
    ) {
        var member = memberService.kakaoLogin(request.getAccessToken());
        return new BaseResponse<>(tokenService.createToken(member.getId()));
    }

    @GetMapping("/member")
    public BaseResponse<?> getMember(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "nickname") String nickname,
            @RequestParam(name = "date") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$") String date,
            @RequestParam(name = "state") @Pattern(regexp = "^(ACTIVATED|SUSPENDED|CANCELLED)$") String state,
            @RequestParam(name = "pageIndex") int pageIndex,
            @RequestParam(name = "size") int size
    ) {
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return new BaseResponse<>(memberService.getMembers(name, nickname, date, state, pageRequest));
    }
}
