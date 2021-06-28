package com.limbae.pfy.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.Subject;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = {"/security"})
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/create/token")
    public Map<String,Object> createToken(@RequestParam("subject") String subject){

        Map<String, String> data = new HashMap<>();
        data.put("role","empolyer");
        data.put("email","waspy@naver.com");
        String token = securityService.createToken(data);

        Map<String, Object> map = new HashMap<>();
        map.put("result", token);
        return map;
    }

    @GetMapping("/get/subject")
    public Map<String, Object> getSubject(@RequestParam("token")String token){
        Map<String, Object> data = securityService.getSubject(token);
        Map<String, Object> map = new HashMap<>();
        map.put("role", data.get("role"));
        map.put("email", data.get("email"));
        return map;
    }


}
