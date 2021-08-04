package com.limbae.pfy.controller;

import com.limbae.pfy.domain.ImageVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.UserDTO;
import com.limbae.pfy.service.ImageService;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {

    UserService userService;
    ImageService imageService;
    EntityUtil entityUtil;
    private final String serverUri;

    public UserController(UserService userService, ImageService imageService, EntityUtil entityUtil,
                          @Value("${server.uri}")String serverUri) {
        this.userService = userService;
        this.imageService = imageService;
        this.entityUtil = entityUtil;
        this.serverUri = serverUri;
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseObjectDTO> signup(
            @Valid @RequestBody UserDTO userDto) throws DuplicateMemberException {

        //it can throw DuplicateMemberException
        userService.signUp(userDto);

        return new ResponseEntity<>(new ResponseObjectDTO("signup success"),  HttpStatus.CREATED);
    }

    @GetMapping("/userInfo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDTO> getMyUserInfo() throws AuthException {

        //it can throw AuthException
        UserVO userVO = userService.getMyUserWithAuthorities();

        UserDTO userDTO = entityUtil.convertUserVoToDto(userVO);
        //이미지 주소 연결
        try{
            ImageVO image = imageService.getImageWithName(userVO.getUid() + "_profile_img");
            String uri = serverUri + "/api/img/default/" + image.getName();
            userDTO.setImg(uri);
        }catch (NotFoundException e){
            userDTO.setImg("not registered");
        }

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/valid")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResponseObjectDTO> userValidCheck() {
        return ResponseEntity.ok(new ResponseObjectDTO("true"));
    }
}