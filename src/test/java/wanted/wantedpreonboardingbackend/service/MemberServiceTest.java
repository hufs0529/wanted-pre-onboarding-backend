package wanted.wantedpreonboardingbackend.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import wanted.wantedpreonboardingbackend.domain.member.dto.MemberRequestDto;
import wanted.wantedpreonboardingbackend.domain.member.entity.Member;
import wanted.wantedpreonboardingbackend.domain.member.repository.MemberRepository;
import wanted.wantedpreonboardingbackend.domain.member.service.MemberService;
import wanted.wantedpreonboardingbackend.global.exception.DuplicateEmailException;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceTest {




    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    String EMAIL = "example@example.com";
    String PASSWORD = "password12!";

    private void clear() {
        em.flush();
        em.clear();
    }

    private MemberRequestDto makeMemberSignUpDto() {
        return new MemberRequestDto(EMAIL, PASSWORD);
    }

    private MemberRequestDto setMember() throws Exception {
        MemberRequestDto memberSignUpDto = makeMemberSignUpDto();
        memberService.signUp(memberSignUpDto);
        clear();
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();

        emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
                .username(memberSignUpDto.getEmail())
                .password(memberSignUpDto.getPassword())
                .build(),
                null, null));

        SecurityContextHolder.setContext(emptyContext);
        return memberSignUpDto;
    }

    @AfterEach
    public void removeMember() {
        SecurityContextHolder.createEmptyContext().setAuthentication(null);
    }

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        MemberRequestDto memberSignUpDto = makeMemberSignUpDto();

        //when
        memberService.signUp(memberSignUpDto);
        clear();

        //then
        Member member = memberRepository.findByEmail(memberSignUpDto.getEmail());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(memberSignUpDto.getEmail());
    }

    @Test
    public void 회원가입_실패_원인_이메일중복() throws Exception {
        //given
        MemberRequestDto memberSignUpDto = makeMemberSignUpDto();
        memberService.signUp(memberSignUpDto);
        clear();

        //when, then
        Exception exception = assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto));
        assertThat(assertThrows(DuplicateEmailException.class, () -> memberService.signUp(memberSignUpDto)));
    }

//    @Test
//    public void 회원가입_실패_입력하지않은_필드가있으면_오류() throws Exception {
//        //given
//        MemberRequestDto memberSignUpDto1 = new MemberRequestDto(null,passwordEncoder.encode(PASSWORD));
//        MemberRequestDto memberSignUpDto2 = new MemberRequestDto("username",null);
//
//        //when, then
////        Exception exception = assertThrows(Exception.class, () -> memberService.signUp(memberSignUpDto1));
////        assertThat(assertThrows(ValidationException.class, () -> memberService.signUp(memberSignUpDto1)));
//
//        ValidationException exception2 = assertThrows(ValidationException.class, () -> memberService.signUp(memberSignUpDto2));
//        assertThat(exception2.getMessage()).isEqualTo("Expected error message");
//    }
}