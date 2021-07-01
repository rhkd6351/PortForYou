package com.limbae.pfy.service;

import com.limbae.pfy.domain.AuthorityVO;
import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.PortfolioListDto;
import com.limbae.pfy.dto.UserDto;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public List<PortfolioListDto> getMyPortfolios(){
        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithPortfolioByUsername);
        List<PortfolioListDto> list;
        if(uvo.isPresent()){
            list = uvo.get().getPortfolio()
                    .stream()
                    .map(i -> PortfolioListDto.builder()
                            .title(i.getTitle())
                            .content(i.getContent())
                            .reg_date(i.getRegDate())
                            .idx(i.getIdx())
                            .build()).collect(Collectors.toList());
            return list;
        }else{
            return null;
        }
    }
}
