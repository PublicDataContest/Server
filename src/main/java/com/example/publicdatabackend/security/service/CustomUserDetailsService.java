package com.example.publicdatabackend.security.service;

import com.example.publicdatabackend.member.domain.Member;
import com.example.publicdatabackend.member.repository.UserRepository;
import com.example.publicdatabackend.security.jwt.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//UserDetails객체로 변환하기 위한 클래스
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //사용자의 이름(userName)을 받아와, DB에서 찾고, UserDetails 객체로 변환 후 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member user = userRepository.findByUserName(username);
        return new UserPrincipal(user.getId(), user.getUserName(), user.getPassword());
    }
}
