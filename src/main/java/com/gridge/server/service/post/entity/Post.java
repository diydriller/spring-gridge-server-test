package com.gridge.server.service.post.entity;

import com.gridge.server.service.common.entity.BaseEntity;
import com.gridge.server.service.common.entity.DeleteState;
import com.gridge.server.service.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "delete_state")
    @Enumerated(EnumType.STRING)
    private DeleteState deleteState;

    public void setPostImages(List<PostImage> postImages){
        this.postImages = postImages;
        postImages.forEach(postImage -> postImage.setPost(this));
    }
}
