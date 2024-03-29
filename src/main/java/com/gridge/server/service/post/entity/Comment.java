package com.gridge.server.service.post.entity;

import com.gridge.server.service.common.entity.BaseEntity;
import com.gridge.server.service.common.entity.DeleteState;
import com.gridge.server.service.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Column(name = "delete_state")
    @Enumerated(EnumType.STRING)
    private DeleteState deleteState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setPost(Post post){
        this.post = post;
        this.post.getComments().add(this);
    }

    public void changeContent(String content){
        this.content = content;
    }
    public void delete(){
        this.deleteState = DeleteState.DELETED;
    }
}
