package com.gridge.server.service.member;

import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.service.common.SecurityService;
import com.gridge.server.service.member.dto.MemberInfo;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.sns.KakaoRestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.gridge.server.service.member.entity.MemberState.ACTIVATED;
import static com.gridge.server.service.member.entity.MemberType.KAKAO;

@Service
@RequiredArgsConstructor
public class KakaoMemberService {
    private final KakaoRestClientService kakaoRestClientService;
    private final MemberRepository memberRepository;
    private final SecurityService securityService;

    public MemberInfo createKakaoMember(String accessToken) {
        var kakaoAccountInfo = kakaoRestClientService.getKakaoInfo(accessToken);
        var memberInfo = MemberInfo.builder()
                .nickname(Objects.requireNonNull(kakaoAccountInfo.getEmail()))
                .imageUrl(Objects.requireNonNull(kakaoAccountInfo.getProfile()).getProfile_image_url())
                .name(Objects.requireNonNull(kakaoAccountInfo.getProfile()).getNickname())
                .build();
        var member = Member.builder()
                .nickname(securityService.twoWayEncrypt(memberInfo.getNickname()))
                .imageUrl(memberInfo.getImageUrl())
                .name(securityService.twoWayEncrypt(memberInfo.getName()))
                .state(ACTIVATED)
                .type(KAKAO)
                .build();
        memberRepository.save(member);
        return memberInfo;
    }
}
