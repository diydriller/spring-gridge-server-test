package com.gridge.server.dataManager.post;

import com.gridge.server.service.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    @Query("SELECT p FROM Post p")
    List<Post> findAllPost(Pageable pageable);
}
