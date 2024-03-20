package com.gridge.server.dataManager.member;

import com.gridge.server.service.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    boolean existsByNickname(String nickname);
    @Query("SELECT m FROM Member m WHERE m.nickname = :nickname AND m.state = 'ACTIVATED'")
    Optional<Member> findByNickname(
            @Param("nickname") String nickname
    );
    @Query("SELECT m FROM Member m WHERE m.id = :id AND m.state = 'ACTIVATED'")
    Optional<Member> findById(
            @Param("id") Long id
    );
}
