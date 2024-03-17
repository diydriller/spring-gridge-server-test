package com.gridge.server.service.post.entity;

import jakarta.persistence.*;

@Entity
public class PostImage {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
