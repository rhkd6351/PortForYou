package com.limbae.pfy.controller;

import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.UserDTO;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {
    private final UserService userService;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<ResponseObjectDTO> signup(
            @Valid @RequestBody UserDTO userDto) {
        try {
            UserVO signup = userService.signup(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseObjectDTO("duplicated"), null, HttpStatus.FORBIDDEN); //403
        }
        return new ResponseEntity<>(new ResponseObjectDTO("true"), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/userInfo", method = {RequestMethod.GET})
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDTO> getMyUserInfo() {
        return ResponseEntity.ok(entityUtil.convertUserVoToDto(userService.getMyUserWithAuthorities().get()));
    }

    @GetMapping("/valid")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResponseObjectDTO> userValidCheck() {

        return ResponseEntity.ok(new ResponseObjectDTO("true"));
    }



//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<UserVO> getUserInfo(@PathVariable String username) {
//        if(userService.getUserWithAuthorities(username).isPresent()){
//            return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
//        }else{
//            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
//        }
//    }
}