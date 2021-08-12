package com.limbae.pfy.service.user;

import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.user.UserDTO;
import javassist.bytecode.DuplicateMemberException;

import javax.security.auth.message.AuthException;

public interface UserServiceInterface {

    UserVO update(UserDTO userDto) throws Exception;

    UserVO update(UserVO userVO) throws Exception;

    void delete(String password) throws Exception;

    UserVO getByAuth() throws AuthException;

    UserVO getByUid(Long uid) throws Exception;

}
