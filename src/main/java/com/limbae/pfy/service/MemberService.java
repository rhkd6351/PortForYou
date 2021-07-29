package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.*;
import com.limbae.pfy.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberService {

    PortfolioRepository portfolioRepository;
    PositionRepository positionRepository;
    UserRepository userRepository;
    StackRepository stackRepository;
    EducationRepository educationRepository;
    MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, PortfolioRepository portfolioRepository, PositionRepository positionRepository, UserRepository userRepository, StackRepository stackRepository, EducationRepository educationRepository) {
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.stackRepository = stackRepository;
        this.educationRepository = educationRepository;
        this.memberRepository = memberRepository;
    }


    public MemberVO saveMember(MemberVO vo){
        return memberRepository.save(vo);
    }

}
