package com.gridge.server.service.member;

import com.gridge.server.service.common.BaseEntity;
import com.gridge.server.service.post.Comment;
import com.gridge.server.service.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String nickname;
    private String password;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private MemberState state;
    @Column(name = "image_url")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private MemberType type;

    @OneToMany(mappedBy = "member")
    private Set<Post> posts;
    @OneToMany(mappedBy = "member")
    private Set<Comment> comments;
}
