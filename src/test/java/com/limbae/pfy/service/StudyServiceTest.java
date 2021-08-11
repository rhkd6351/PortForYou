//package com.limbae.pfy.service;
//
//import com.limbae.pfy.domain.user.PortfolioVO;
//import com.limbae.pfy.domain.study.StudyVO;
//import com.limbae.pfy.dto.user.PortfolioDTO;
//import com.limbae.pfy.dto.user.PortfolioListDTO;
//import com.limbae.pfy.dto.etc.ProjectDTO;
//import com.limbae.pfy.dto.study.StudyDTO;
//import com.limbae.pfy.jwt.TokenProvider;
//import com.limbae.pfy.repository.user.PortfolioRepository;
//import com.limbae.pfy.repository.user.UserRepository;
//import com.limbae.pfy.util.EntityUtil;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@SpringBootTest
//@Transactional
//public class StudyServiceTest {
//
//    AuthenticationManagerBuilder authenticationManagerBuilder;
//    TokenProvider tokenProvider;
//    EntityUtil entityUtil;
//    StudyService studyService;
//
//    @Autowired
//    public StudyServiceTest(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, EntityUtil entityUtil, StudyService studyService) {
//        this.authenticationManagerBuilder = authenticationManagerBuilder;
//        this.tokenProvider = tokenProvider;
//        this.entityUtil = entityUtil;
//        this.studyService = studyService;
//    }
//
//    @DisplayName("")
//    @Test
//    void 스터디테스트하기() {
//        //given
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken("kwang", "kwang");
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        //when
//
//        List<StudyVO> myStudyList = studyService.getMyStudyList();
//
//        //then
//        Assertions.assertThat(myStudyList).isNotEmpty();
//    }
//}
