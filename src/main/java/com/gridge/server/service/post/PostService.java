package com.gridge.server.service.post;

import com.gridge.server.common.exception.BaseException;
import com.gridge.server.dataManager.post.CommentRepository;
import com.gridge.server.dataManager.post.PostRepository;
import com.gridge.server.service.common.FileService;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.dto.CommentInfo;
import com.gridge.server.service.post.dto.PostInfo;
import com.gridge.server.service.post.entity.Comment;
import com.gridge.server.service.post.entity.Post;
import com.gridge.server.service.post.entity.PostImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.gridge.server.common.response.BaseResponseState.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final CommentRepository commentRepository;

    @Transactional
    public PostInfo createPost(PostInfo info, List<MultipartFile> files, Member member) {
        List<PostImage> postImages = files.stream()
                .map(file -> {
                    try {
                        fileService.checkImageType(file);
                        String imageUrl = fileService.saveFile(file);
                        info.getImageUrls().add(imageUrl);
                        return PostImage.builder()
                                .imageUrl(imageUrl)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
        var post = Post.builder()
                .content(info.getContent())
                .build();
        post.setPostImages(postImages);
        post.setMember(member);
        postRepository.save(post);

        info.setId(post.getId());
        return info;
    }

    @Transactional(readOnly = true)
    public List<PostInfo> getPosts(int pageIndex, int size) {
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return postRepository.findAllPost(pageRequest).stream()
                .map(PostInfo::from)
                .toList();
    }

    @Transactional
    public CommentInfo createComment(Long postId, CommentInfo info, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        var comment = Comment.builder()
                .member(member)
                .content(info.getContent())
                .build();
        comment.setPost(post);
        commentRepository.save(comment);
        info.setId(comment.getId());
        return info;
    }

    @Transactional(readOnly = true)
    public List<CommentInfo> getComments(Long postId, int pageIndex, int size) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.Direction.DESC, "createAt");
        return commentRepository.findAllCommentByPost(post, pageRequest).stream()
                .map(CommentInfo::from)
                .toList();
    }
}
