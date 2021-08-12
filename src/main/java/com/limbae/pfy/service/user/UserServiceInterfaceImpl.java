package com.limbae.pfy.service.user;

import com.limbae.pfy.domain.user.Authority;
import com.limbae.pfy.domain.user.AuthorityVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.user.UserDTO;
import com.limbae.pfy.repository.user.UserRepository;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestValueException;

import javax.security.auth.message.AuthException;
import java.util.*;

@Service
public class UserServiceInterfaceImpl implements UserServiceInterface{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceInterfaceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserVO update(UserDTO userDto) throws Exception {

        AuthorityVO authorityVO =   AuthorityVO.builder()
                .authority(Authority.ROLE_USER)
                .idx(1)
                .build();

        UserVO user = null;

        if(userDto.getUid() != null){
            user = this.getByAuth();

            if(userDto.getPassword() != null)
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            else if(userDto.getName() != null)
                user.setName(userDto.getName());
            else if(userDto.getPhone() != null)
                user.setPhone(userDto.getPhone());
            else if(userDto.getSite() != null)
                user.setSite(userDto.getSite());
            else
                throw new MissingRequestValueException("no data find");

        }else{
            if(userRepository.findWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null)
                throw new DuplicateMemberException("duplicated");
            user = UserVO.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .name(userDto.getName())
                    .phone(userDto.getPhone())
                    .authorities(Collections.singletonList(authorityVO))
                    .activated(true)
                    .build();
        }

        return userRepository.save(user);
    }

    @Override
    public UserVO update(UserVO userVO) throws Exception {
        return userRepository.save(userVO);
    }

    @Override
    public void delete(String password) throws Exception {
        UserVO user = this.getByAuth();
        if(!passwordEncoder.matches(password,user.getPassword()))
            throw new AuthException("invalid password");

        userRepository.delete(user);
    }


    @Override
    public UserVO getByAuth() throws AuthException{
        Optional<UserVO> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findWithAuthoritiesByUsername);
        if(user.isEmpty())
            throw new AuthException("invalid token");
        return user.get();
    }

    @Override
    public UserVO getByUid(Long uid) throws Exception {
        Optional<UserVO> user = userRepository.findByUid(uid);
        if(user.isEmpty())
            throw new NotFoundException("invalid uid");

        return user.get();
    }
}
