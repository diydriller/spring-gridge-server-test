package com.gridge.server.service.post;

import com.gridge.server.common.exception.AuthorizationException;
import com.gridge.server.common.exception.BaseException;
import com.gridge.server.common.util.TimeUtil;
import com.gridge.server.dataManager.member.MemberRepository;
import com.gridge.server.dataManager.post.CommentRepository;
import com.gridge.server.dataManager.post.PostReportRepository;
import com.gridge.server.dataManager.post.PostRepository;
import com.gridge.server.dataManager.post.PostSpecification;
import com.gridge.server.service.common.FileService;
import com.gridge.server.service.member.entity.Member;
import com.gridge.server.service.post.dto.CommentInfo;
import com.gridge.server.service.post.dto.PostInfo;
import com.gridge.server.service.post.dto.PostReportInfo;
import com.gridge.server.service.post.entity.Comment;
import com.gridge.server.service.post.entity.Post;
import com.gridge.server.service.post.entity.PostImage;
import com.gridge.server.service.post.entity.PostReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.gridge.server.common.response.BaseResponseState.*;
import static com.gridge.server.service.common.entity.DeleteState.NOT_DELETED;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final CommentRepository commentRepository;
    private final PostReportRepository postReportRepository;
    private final MemberRepository memberRepository;

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
                .deleteState(NOT_DELETED)
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
    public PostInfo updatePost(Long postId, PostInfo info, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        if(!Objects.equals(post.getMember().getId(), member.getId())){
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
        post.changeContent(info.getContent());
        postRepository.save(post);
        info.setId(post.getId());
        return info;
    }

    @Transactional
    public void deletePost(Long postId, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        if(!Objects.equals(post.getMember().getId(), member.getId())){
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
        post.delete();
        postRepository.save(post);
    }

    @Transactional
    public CommentInfo createComment(Long postId, CommentInfo info, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        var comment = Comment.builder()
                .member(member)
                .content(info.getContent())
                .deleteState(NOT_DELETED)
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
        return commentRepository.findAllComment(post, pageRequest).stream()
                .map(CommentInfo::from)
                .toList();
    }

    @Transactional
    public CommentInfo updateComment(Long postId, Long commentId, CommentInfo info, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        var comment = commentRepository.findComment(post, commentId)
                .orElseThrow(() -> new BaseException(COMMENT_NOT_FOUND));
        if(!Objects.equals(comment.getMember().getId(), member.getId())){
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
        comment.changeContent(info.getContent());
        commentRepository.save(comment);
        info.setId(comment.getId());
        return info;
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        var comment = commentRepository.findComment(post, commentId)
                .orElseThrow(() -> new BaseException(COMMENT_NOT_FOUND));
        if(!Objects.equals(comment.getMember().getId(), member.getId())){
            throw new AuthorizationException(AUTHORIZATION_ERROR);
        }
        comment.delete();
        commentRepository.save(comment);
    }

    @Transactional
    public void reportPost(Long postId, PostReportInfo info, Member member) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        var postReport = PostReport.builder()
                .content(info.getContent())
                .build();
        postReport.setPost(post);
        postReport.setMember(member);
        postReportRepository.save(postReport);
        long reportCount = postReportRepository.countPostReport(post);
        if(reportCount >= 10){
            post.delete();
            postRepository.save(post);
        }
    }

    @Transactional(readOnly = true)
    public List<PostInfo> getPosts(String nickname, String dateString, String state, PageRequest pageRequest) {
        LocalDate date = TimeUtil.stringToLocalDate(dateString);
        var member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BaseException(MEMBER_NOT_FOUND));
        var spec = Specification.where((PostSpecification.equalMember(member))
                .and(PostSpecification.betweenCreateDate(TimeUtil.startOfDate(date), TimeUtil.endOfDate(date)))
                .and(PostSpecification.equalState(state)));
        return postRepository.findAll(spec, pageRequest).stream()
                .map(PostInfo::from).toList();
    }

    @Transactional(readOnly = true)
    public PostInfo getPost(long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        return PostInfo.from(post);
    }

    @Transactional
    public void deletePost(long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new BaseException(POST_NOT_FOUND));
        post.delete();
        postRepository.save(post);
        postRepository.deleteAllComment(post);
        postRepository.deleteAllPostImage(post);
    }
}
