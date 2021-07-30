package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.UserDTO;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.util.SecurityUtil;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.*;

@Service
public class UserService implements UserServiceInterface{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserVO signUp(UserDTO userDto) throws DuplicateMemberException{
        if(userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null)
            throw new DuplicateMemberException("duplicated");

        AuthorityVO authorityVO =   AuthorityVO.builder()
                .authority(Authority.ROLE_USER)
                .idx(1)
                .build();

        UserVO userVO = UserVO.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .phone(userDto.getPhone())
                .authorities(Collections.singletonList(authorityVO))
                .activated(true)
                .build();

        return userRepository.save(userVO);
    }

    public UserVO getMyUserWithAuthorities() throws AuthException{
        Optional<UserVO> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(user.isEmpty())
            throw new AuthException("invalid token");
        return user.get();
    }
}
