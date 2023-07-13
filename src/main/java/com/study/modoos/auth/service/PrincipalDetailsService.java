package com.study.modoos.auth.service;

import com.study.modoos.member.entity.Member;
import com.study.modoos.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberService memberService;
    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberService.findByEmail(email);

        if(findMember != null){
            return new PrincipalDetails(findMember);
        }

        return null;
    }

}
