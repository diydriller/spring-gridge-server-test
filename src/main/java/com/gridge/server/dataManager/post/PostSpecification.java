package com.gridge.server.dataManager.post;

import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.entity.Post;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PostSpecification {
    public static Specification<Post> equalMember(Member member) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("member"), member);
    }
    public static Specification<Post> equalState(String state) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("deleteState"), state);
    }
    public static Specification<Post> betweenCreateDate(LocalDateTime start, LocalDateTime end) {
        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.between(root.get("createAt"), start, end);
    }
}
