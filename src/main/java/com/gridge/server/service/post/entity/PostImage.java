package com.gridge.server.service.post.entity;

import com.gridge.server.service.common.entity.BaseEntity;
import com.gridge.server.service.common.entity.DeleteState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "delete_state")
    @Enumerated(EnumType.STRING)
    private DeleteState deleteState;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
