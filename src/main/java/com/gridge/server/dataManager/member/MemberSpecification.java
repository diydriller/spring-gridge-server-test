package com.gridge.server.dataManager.member;

import com.gridge.server.service.member.entity.Member;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class MemberSpecification {
    public static Specification<Member> equalName(String name) {
        return (Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }
    public static Specification<Member> equalNickname(String nickname) {
        return (Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("nickname"), nickname);
    }
    public static Specification<Member> betweenCreateDate(LocalDateTime start, LocalDateTime end) {
        return (Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.between(root.get("createAt"), start, end);
    }
    public static Specification<Member> equalState(String state) {
        return (Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), state);
    }
}
