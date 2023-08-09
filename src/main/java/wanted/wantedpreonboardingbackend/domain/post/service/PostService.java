package wanted.wantedpreonboardingbackend.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.wantedpreonboardingbackend.domain.member.entity.Member;
import wanted.wantedpreonboardingbackend.domain.member.repository.MemberRepository;
import wanted.wantedpreonboardingbackend.domain.post.dto.*;
import wanted.wantedpreonboardingbackend.domain.post.entity.Post;
import wanted.wantedpreonboardingbackend.domain.post.repository.CustomPostRepository;
import wanted.wantedpreonboardingbackend.domain.post.repository.PostRepository;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CustomPostRepository customPostRepository;


    public String checkWriter(Post post) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        return email;
    }


    public Post save(PostSaveDto postSaveDto) throws Exception {
        Post post = postSaveDto.toEntity();
        Member member = memberRepository.findByEmail(checkWriter(post));
        post.confirmWriter(member);

        if(member.getEmail() == post.getWriter().getEmail()){
            postRepository.save(post);
        }else{
            throw new Exception("로그인된 사용자가 없습니다");
        }
        return post;
    }

    public PostPagingDto getPostList(Pageable pageable, PostSearchCondition postSearchCondition) {
        return new PostPagingDto(customPostRepository.search(postSearchCondition, pageable));
    }


    public PostInfoDto getPostInfo(Long postId) throws Exception {
        return new PostInfoDto(postRepository.findWriterById(postId)
                .orElseThrow(()-> new Exception()));

    }

    public PostUpdateDto update(Long postId, PostUpdateDto postUpdateDto) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception());
        Member member = memberRepository.findByEmail(checkWriter(post));

        if (member.getEmail().equals(post.getWriter().getEmail())) {
            if (postUpdateDto.title().isPresent()) {
                post.setTitle(postUpdateDto.title().get());
            }
            if (postUpdateDto.content().isPresent()) {
                post.setContent(postUpdateDto.content().get());
            }
        } else {
            throw new Exception("로그인된 사용자가 없습니다");
        }
        return postUpdateDto;
    }

    public String delete(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception());
        Member member = memberRepository.findByEmail(checkWriter(post));

        if (member.getEmail().equals(post.getWriter().getEmail())){
            postRepository.deleteById(postId);
        }else {
            throw new Exception("로그인된 사용자가 없습니다");
        }
        return postId + " id의 게시물이 지워졌습니다";
    }
}
