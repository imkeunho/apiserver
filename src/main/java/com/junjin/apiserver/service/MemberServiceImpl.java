package com.junjin.apiserver.service;

import com.junjin.apiserver.domain.Member;
import com.junjin.apiserver.domain.MemberRole;
import com.junjin.apiserver.dto.MemberDTO;
import com.junjin.apiserver.dto.MemberModifyDTO;
import com.junjin.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO getKakaoMember(String accessToken) {

        //accessToken 이용해서 사용자 정보 가져오기
        //카카오 연동 닉네임 -- 이메일 주소에 해당
        String nickname = getEmailFromKakaoAccessToken(accessToken);

        Optional<Member> result = memberRepository.findById(nickname);

        if (result.isPresent()) {

            log.info("existed............");

            return entityToDTO(result.get());
        }

        Member socialMember = makeSocialMember(nickname);

        memberRepository.save(socialMember);

        return entityToDTO(socialMember);

    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {

        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());

        Member member = result.orElseThrow();

        member.changeNickname(memberModifyDTO.getNickname());
        member.changeSocial(false);
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));

        memberRepository.save(member);
    }

    private Member makeSocialMember(String email) {

        String tempPassword = makeTempPassword();

        log.info("tempPassword: {}", tempPassword);

        Member member = Member.builder()
                .email(email)
                .pw(passwordEncoder.encode(tempPassword))
                .nickname("Social Member")
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    private String getEmailFromKakaoAccessToken(String accessToken) {

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriBuilder.toUri(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response----------------------");
        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties");

        log.info("kakaoAccount : {}", kakaoAccount);

        String nickname = kakaoAccount.get("nickname");
        log.info("nickName : {}", nickname);

        return nickname;
    }

    private String makeTempPassword() {

        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < 10; i++){
            buffer.append( (char) ( (int)(Math.random()*55) + 65 ));
        }

        return buffer.toString();
    }
}
