package wanted.wantedpreonboardingbackend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import wanted.wantedpreonboardingbackend.domain.member.dto.MemberRequestDto;
import wanted.wantedpreonboardingbackend.domain.member.service.MemberService;
import wanted.wantedpreonboardingbackend.domain.post.dto.PostSaveDto;
import wanted.wantedpreonboardingbackend.domain.post.entity.Post;
import wanted.wantedpreonboardingbackend.domain.post.service.PostService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class PostServiceImplTest {


    @Autowired private EntityManager em;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    private void clear(){
        em.flush();
        em.clear();
    }

    private String title = "제목";
    private String content = "내용";

    private static final String EMAIL = "username@example.com";
    private static final String PASSWORD = "PASSWORD123@@@";

    private void setAnotherAuthentication() throws Exception {
        memberService.signUp(new MemberRequestDto(EMAIL,PASSWORD));
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        emptyContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        User.builder()
                                .username(EMAIL)
                                .password(PASSWORD)
                                .build(),
                        null)
        );
        SecurityContextHolder.setContext(emptyContext);
        clear();
    }

    private Post findPost() {
        return em.createQuery("select p from Post p", Post.class).getSingleResult();
    }

    @Test
    public void 포스트삭제_실패() throws Exception {
        PostSaveDto postSaveDto = new PostSaveDto(title, content);

        postService.save(postSaveDto);
        clear();

        //when, then
        setAnotherAuthentication();
        Post findPost = findPost();
        //assertThrows(ValidationException.class, ()-> postService.delete(findPost.getId()));
    }

}
