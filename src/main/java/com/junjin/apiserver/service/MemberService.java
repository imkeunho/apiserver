package com.junjin.apiserver.service;

import com.junjin.apiserver.domain.Member;
import com.junjin.apiserver.dto.MemberDTO;
import com.junjin.apiserver.dto.MemberModifyDTO;
import jakarta.transaction.Transactional;

import java.util.stream.Collectors;

@Transactional
public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member) {

        return new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream()
                        .map(Enum::name).collect(Collectors.toList()));
    }
}
