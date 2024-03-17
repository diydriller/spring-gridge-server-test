package com.gridge.server.service.member;

import com.gridge.server.common.exception.BaseException;
import com.gridge.server.service.common.SecurityService;
import com.gridge.server.common.util.TimeUtil;
import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.service.member.dto.MemberInfo;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.sns.KakaoRestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.gridge.server.common.response.BaseResponseState.*;
import static com.gridge.server.service.member.entity.MemberState.ACTIVATED;
import static com.gridge.server.service.member.entity.MemberType.KAKAO;
import static com.gridge.server.service.member.entity.MemberType.LOCAL;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityService securityService;
    private final KakaoRestClientService kakaoRestClientService;

    @Transactional
    public void createMember(MemberInfo memberInfo) {
        checkNicknameExist(memberInfo.getNickname());
        var member = Member.builder()
                .nickname(securityService.twoWayEncrypt(memberInfo.getNickname()))
                .password(securityService.oneWayEncrypt(memberInfo.getPassword()))
                .phoneNumber(securityService.twoWayEncrypt(memberInfo.getPhoneNumber()))
                .name(securityService.twoWayEncrypt(memberInfo.getName()))
                .birthday(TimeUtil.stringToLocalDate(memberInfo.getBirthday()))
                .state(ACTIVATED)
                .type(LOCAL)
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void checkNicknameExist(String nickname) {
        if(memberRepository.existsByNickname(securityService.twoWayEncrypt(nickname))){
            throw new BaseException(NICKNAME_DUPLICATED);
        }
    }

    @Transactional
    public Member login(MemberInfo memberInfo) {
        var member = memberRepository.findByNickname(securityService.twoWayEncrypt(memberInfo.getNickname()))
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        if(securityService.isOneWayEncryptionMatch(memberInfo.getPassword(), member.getPassword())) {
            throw new BaseException(PASSWORD_NOT_MATCH);
        }
        return member;
    }

    @Transactional
    public MemberInfo createKakaoMember(String accessToken) {
        var kakaoAccountInfo = kakaoRestClientService.getKakaoInfo(accessToken);
        var memberInfo = MemberInfo.builder()
                .nickname(Objects.requireNonNull(kakaoAccountInfo.getEmail()))
                .imageUrl(Objects.requireNonNull(kakaoAccountInfo.getProfile()).getProfile_image_url())
                .name(Objects.requireNonNull(kakaoAccountInfo.getProfile()).getNickname())
                .build();
        checkNicknameExist(memberInfo.getNickname());
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
