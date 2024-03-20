package com.gridge.server.dataManager.post;

import com.gridge.server.service.post.entity.Post;
import com.gridge.server.service.post.entity.PostReport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    @Query("SELECT COUNT(pr) FROM PostReport pr WHERE pr.post = :post GROUP BY pr.member.id")
    long countPostReport(@Param("post") Post post);
    @Query("SELECT pr FROM PostReport pr JOIN FETCH pr.member WHERE pr.deleteState = 'NOT_DELETED'")
    List<PostReport> findAllPostReport(PageRequest pageRequest);
    @Query("SELECT pr FROM PostReport pr WHERE pr.id = :id AND pr.deleteState = 'NOT_DELETED'")
    Optional<PostReport> findById(@Param("id") long id);
}
