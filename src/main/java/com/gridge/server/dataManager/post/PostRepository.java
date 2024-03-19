package com.gridge.server.dataManager.post;

import com.gridge.server.service.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deleteState = 'NOT_DELETED'")
    Optional<Post> findById(
            @Param("id") Long id
    );
    @Query("SELECT p FROM Post p WHERE p.deleteState = 'NOT_DELETED'")
    List<Post> findAllPost(Pageable pageable);
}
