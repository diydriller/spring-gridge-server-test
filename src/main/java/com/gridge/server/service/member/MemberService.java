package com.gridge.server.service.member;

import com.gridge.server.common.exception.BaseException;
import com.gridge.server.common.security.SecurityService;
import com.gridge.server.common.util.TimeUtil;
import com.gridge.server.dataManager.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gridge.server.common.response.BaseResponseState.NICKNAME_DUPLICATED;
import static com.gridge.server.service.member.MemberState.ACTIVATED;
import static com.gridge.server.service.member.MemberType.LOCAL;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityService securityService;

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
}
