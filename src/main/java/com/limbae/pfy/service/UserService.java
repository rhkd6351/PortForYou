package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.PortfolioListDto;
import com.limbae.pfy.dto.PositionDTO;
import com.limbae.pfy.dto.StackDTO;
import com.limbae.pfy.dto.UserDto;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Transactional
    public UserVO signup(UserDto userDto){
        if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null){
            throw new RuntimeException("이미 가입되어있는 유저입니다.");
        }

        AuthorityVO authorityVO =   AuthorityVO.builder()
                .authorityName("ROLE_USER")
                .idx(1)
                .build();

        UserVO userVO = UserVO.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .authorities(Collections.singleton(authorityVO))
                .activated(true)
                .build();

        return userRepository.save(userVO);
    }



//    @Transactional(readOnly = true)
//    public Optional<UserVO> getUserWithAuthorities(String username){
//        return userRepository.findOneWithAuthoritiesByUsername(username);
//    }


    public Optional<UserVO> getUserWithPortfoliosByUsername(String username){
        return userRepository.findOneWithPortfolioByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<UserVO> getMyUserWithAuthorities(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    @Transactional(readOnly = true)
    public Optional<UserVO> getMyUserWithPortfolios(){
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithPortfolioByUsername);
    }
}
