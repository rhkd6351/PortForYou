package com.limbae.pfy.controller;

import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.UserDTO;
import com.limbae.pfy.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Log4j2
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseObjectDTO> signup(
            @Valid @RequestBody UserDTO userDto) {
        try {
            UserVO signup = userService.signup(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseObjectDTO("username duplicated"), null, HttpStatus.FORBIDDEN); //403
        }
        return new ResponseEntity<>(new ResponseObjectDTO("signup success"), null, HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserVO> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
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