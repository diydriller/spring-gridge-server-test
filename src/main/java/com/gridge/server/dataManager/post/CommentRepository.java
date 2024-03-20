package com.gridge.server.dataManager.post;

import com.gridge.server.service.post.entity.Comment;
import com.gridge.server.service.post.entity.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.deleteState = 'NOT_DELETED'")
    List<Comment> findAllComment(
            @Param("post") Post post,
            PageRequest pageRequest
    );

    @Query("SELECT c FROM Comment c WHERE c.post =:post AND c.id = :commentId AND c.deleteState = 'NOT_DELETED'")
    Optional<Comment> findComment(
            @Param("post") Post post,
            @Param("commentId") Long commentId);
}
