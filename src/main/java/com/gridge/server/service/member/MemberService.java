package com.gridge.server.service.member;

import com.gridge.server.common.exception.BaseException;
import com.gridge.server.dataManager.member.MemberSpecification;
import com.gridge.server.service.common.SecurityService;
import com.gridge.server.common.util.TimeUtil;
import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.service.history.event.HistoryEvent;
import com.gridge.server.service.member.dto.MemberInfo;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.sns.KakaoRestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.gridge.server.common.response.BaseResponseState.*;
import static com.gridge.server.service.history.entity.HistoryType.MEMBER;
import static com.gridge.server.service.member.entity.MemberState.ACTIVATED;
import static com.gridge.server.service.member.entity.MemberType.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityService securityService;
    private final KakaoRestClientService kakaoRestClientService;
    private final ApplicationEventPublisher eventPublisher;

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
                .type(LOCAL_USER)
                .build();
        memberRepository.save(member);

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(member)
                        .type(MEMBER)
                        .build()
        );
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

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(member)
                        .type(MEMBER)
                        .build()
        );
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
                .type(KAKAO_USER)
                .build();
        memberRepository.save(member);
        memberInfo.setId(member.getId());

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(member)
                        .type(MEMBER)
                        .build()
        );
        return memberInfo;
    }

    @Transactional
    public Member kakaoLogin(String accessToken) {
        var kakaoAccountInfo = kakaoRestClientService.getKakaoInfo(accessToken);
        var member = memberRepository.findByNickname(securityService.twoWayEncrypt(Objects.requireNonNull(kakaoAccountInfo.getEmail())))
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        if (member.getType() != KAKAO_USER) {
            throw new BaseException(NOT_KAKAO_MEMBER);
        }
        member.updateLastLoginAt();
        memberRepository.save(member);

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(member)
                        .type(MEMBER)
                        .build()
        );
        return member;
    }

    @Transactional(readOnly = true)
    public List<MemberInfo> getMembersByAdmin(String name, String nickname, String dateString, String state, PageRequest pageRequest, Member me) {
        LocalDate date = TimeUtil.stringToLocalDate(dateString);
        var spec = Specification.where(MemberSpecification.equalName(securityService.twoWayEncrypt(name)))
                .and(MemberSpecification.equalNickname(securityService.twoWayEncrypt(nickname)))
                .and(MemberSpecification.betweenCreateDate(TimeUtil.startOfDate(date), TimeUtil.endOfDate(date)))
                .and(MemberSpecification.equalState(state));
        var info = memberRepository.findAll(spec, pageRequest).stream()
                .map(member -> MemberInfo.builder()
                        .nickname(securityService.twoWayDecrypt(member.getNickname()))
                        .id(member.getId())
                        .imageUrl(member.getImageUrl())
                        .name(securityService.twoWayDecrypt(member.getName()))
                        .phoneNumber(securityService.twoWayDecrypt(member.getPhoneNumber()))
                        .birthday(member.getBirthday().toString())
                        .createAt(member.getCreateAt().toString())
                        .updateAt(member.getUpdateAt().toString())
                        .lastLoginAt(member.getLastLoginAt().toString())
                        .build())
                .toList();

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(me)
                        .type(MEMBER)
                        .build()
        );
        return info;
    }

    @Transactional(readOnly = true)
    public MemberInfo getMemberByAdmin(long id, Member me) {
        var member = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        var result = MemberInfo.builder()
                .nickname(securityService.twoWayDecrypt(member.getNickname()))
                .id(member.getId())
                .imageUrl(member.getImageUrl())
                .name(securityService.twoWayDecrypt(member.getName()))
                .phoneNumber(securityService.twoWayDecrypt(member.getPhoneNumber()))
                .birthday(member.getBirthday().toString())
                .createAt(member.getCreateAt().toString())
                .updateAt(member.getUpdateAt().toString())
                .lastLoginAt(member.getLastLoginAt().toString())
                .build();

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(me)
                        .type(MEMBER)
                        .build()
        );
        return result;
    }

    @Transactional
    public void suspendMemberByAdmin(long id, Member me) {
        var member = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        member.suspend();
        memberRepository.save(member);

        eventPublisher.publishEvent(
                HistoryEvent.builder()
                        .member(me)
                        .type(MEMBER)
                        .build()
        );
    }
}
