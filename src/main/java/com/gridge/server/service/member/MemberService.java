package com.gridge.server.service.member;

import com.gridge.server.common.exception.BaseException;
import com.gridge.server.dataManager.member.MemberSpecification;
import com.gridge.server.service.common.SecurityService;
import com.gridge.server.common.util.TimeUtil;
import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.service.member.dto.MemberInfo;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.sns.KakaoRestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
        if(!securityService.isOneWayEncryptionMatch(memberInfo.getPassword(), member.getPassword())) {
            throw new BaseException(PASSWORD_NOT_MATCH);
        }
        member.updateLastLoginAt();
        memberRepository.save(member);
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
        memberInfo.setId(member.getId());
        return memberInfo;
    }

    @Transactional
    public Member kakaoLogin(String accessToken) {
        var kakaoAccountInfo = kakaoRestClientService.getKakaoInfo(accessToken);
        var member = memberRepository.findByNickname(securityService.twoWayEncrypt(Objects.requireNonNull(kakaoAccountInfo.getEmail())))
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        if (member.getType() != KAKAO) {
            throw new BaseException(NOT_KAKAO_MEMBER);
        }
        member.updateLastLoginAt();
        memberRepository.save(member);
        return member;
    }

    @Transactional(readOnly = true)
    public List<MemberInfo> getMembers(String name, String nickname, String dateString, String state, PageRequest pageRequest) {
        LocalDate date = TimeUtil.stringToLocalDate(dateString);
        var spec = Specification.where(MemberSpecification.equalName(securityService.twoWayEncrypt(name)))
                .and(MemberSpecification.equalNickname(securityService.twoWayEncrypt(nickname)))
                .and(MemberSpecification.betweenCreateDate(TimeUtil.startOfDate(date), TimeUtil.endOfDate(date)))
                .and(MemberSpecification.equalState(state));
        return memberRepository.findAll(spec, pageRequest).stream()
                .map(member -> MemberInfo.builder()
                        .nickname(securityService.twoWayDecrypt(member.getNickname()))
                        .id(member.getId())
                        .imageUrl(member.getImageUrl())
                        .name(securityService.twoWayDecrypt(member.getName()))
                        .phoneNumber(securityService.twoWayDecrypt(member.getPhoneNumber()))
                        .birthday(member.getBirthday().toString())
                        .build())
                .toList();
    }
}
