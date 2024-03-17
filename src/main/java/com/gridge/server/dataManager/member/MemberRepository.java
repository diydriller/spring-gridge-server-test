package com.gridge.server.dataManager.member;

import com.gridge.server.service.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    Optional<Member> findByNickname(String nickname);
}
