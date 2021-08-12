package com.limbae.pfy.controller.user;

import com.limbae.pfy.domain.etc.ImageVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.etc.ResponseObjectDTO;
import com.limbae.pfy.dto.user.UserDTO;
import com.limbae.pfy.service.etc.ImageService;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {

    UserServiceInterfaceImpl userService;
    ImageService imageService;
    EntityUtil entityUtil;
    private final String serverUri;

    public UserController(UserServiceInterfaceImpl userService, ImageService imageService, EntityUtil entityUtil,
                          @Value("${server.uri}")String serverUri) {
        this.userService = userService;
        this.imageService = imageService;
        this.entityUtil = entityUtil;
        this.serverUri = serverUri;
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseObjectDTO> save(
            @Valid @RequestBody UserDTO userDto) throws Exception {

        userService.update(userDto);

        return new ResponseEntity<>(new ResponseObjectDTO("signup success"),  HttpStatus.CREATED);
    }

    @DeleteMapping("/userInfo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResponseObjectDTO> delete(
            @Valid @RequestBody UserDTO userDTO) throws Exception {

        userService.delete(userDTO.getPassword());

        return ResponseEntity.ok(new ResponseObjectDTO("delete success"));
    }

    @PostMapping("/userInfo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResponseObjectDTO> update(
            @Valid @RequestBody UserDTO userDto) throws Exception {

        UserVO user = userService.getByAuth();
        userDto.setUid(user.getUid());

        UserVO updated = userService.update(userDto);

        return new ResponseEntity<>(new ResponseObjectDTO("update success"),  HttpStatus.CREATED);
    }

    @GetMapping("/userInfo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDTO> getMyUserInfo() throws AuthException {

        //it can throw AuthException
        UserVO userVO = userService.getByAuth();

        UserDTO userDTO = entityUtil.convertUserVoToDto(userVO);
        //이미지 주소 연결
        try{
            ImageVO image = imageService.getByName(userVO.getUid() + "_profile_img");
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