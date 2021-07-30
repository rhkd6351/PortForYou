package com.limbae.pfy.service;

import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.UserDTO;
import javassist.bytecode.DuplicateMemberException;

import javax.security.auth.message.AuthException;
import java.util.Optional;

public interface UserServiceInterface {

    UserVO signUp (UserDTO userDto) throws DuplicateMemberException;

    UserVO getMyUserWithAuthorities() throws AuthException;

}
