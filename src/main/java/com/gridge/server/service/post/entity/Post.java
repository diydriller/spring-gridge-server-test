package com.gridge.server.service.post.entity;

import com.gridge.server.service.member.entity.Member;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "post")
    private Set<PostImage> postImages;
    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

}
