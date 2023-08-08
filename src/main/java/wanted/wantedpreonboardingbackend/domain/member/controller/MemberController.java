package wanted.wantedpreonboardingbackend.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.wantedpreonboardingbackend.domain.member.dto.MemberRequestDto;
import wanted.wantedpreonboardingbackend.domain.member.service.MemberService;
import wanted.wantedpreonboardingbackend.global.jwt.JwtFilter;
import wanted.wantedpreonboardingbackend.global.jwt.TokenProvider;
import wanted.wantedpreonboardingbackend.global.jwt.dto.TokenDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;


    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberRequestDto> signUp(@Valid @RequestBody MemberRequestDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return ResponseEntity.ok(memberSignUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        TokenDto tokenDto = memberService.authenticateAndGenerateToken(memberRequestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getToken());
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }


}
