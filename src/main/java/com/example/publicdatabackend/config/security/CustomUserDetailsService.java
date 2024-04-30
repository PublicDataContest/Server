package com.example.publicdatabackend.config.security;

import com.example.publicdatabackend.domain.users.Users;
import com.example.publicdatabackend.repository.UserRepository;
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
        Users user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserPrincipal(user.getId(), user.getUserName(), user.getPassword());
    }
}
