package com.study.modoos.auth.service;

import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public PrincipalDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member findUser = memberRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new UsernameNotFoundException("해당 id를 가진 회원을 찾을 수 없습니다 -> " + id));

        if(findUser != null){
            PrincipalDetails principalDetails = new PrincipalDetails(findUser);
            return  principalDetails;
        }

        return null;
    }

}
