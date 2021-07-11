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
public class PortfolioService {

    PortfolioRepository portfolioRepository;
    PositionRepository positionRepository;
    UserRepository userRepository;
    StackRepository stackRepository;
    EducationRepository educationRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, PositionRepository positionRepository, UserRepository userRepository, StackRepository stackRepository, EducationRepository educationRepository) {
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.stackRepository = stackRepository;
        this.educationRepository = educationRepository;
    }


    public Optional<PortfolioVO> getPortfolioByIdx(int idx){
        return portfolioRepository.findOneWithProjectAndPositionByIdx(idx);
    }

    public boolean checkPossessionOfPortfolio(PortfolioVO pfvo,
                                              List<PortfolioListDTO> pfList){
        PortfolioListDTO any = null;


        for(PortfolioListDTO dto : pfList)
            if(dto.getIdx() == pfvo.getIdx()) any = dto;

        return any != null;
    }

    public PortfolioVO savePortfolio(PortfolioDTO portfolioDTO){
        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);

        Set<ProjectVO> projectSet = portfolioDTO.getProject().stream().map(
                i -> ProjectVO.builder()
                        .title(i.getTitle())
                        .content(i.getContent())
                        .stack(i.getStack() != null ?
                                i.getStack().stream().map(
                                        t -> stackRepository.findOneByIdx(t.getIdx()).get()
                                ).collect(Collectors.toSet())
                                : null)
                        .build()
        ).collect(Collectors.toSet());

        Set<PositionVO> positionVOS =portfolioDTO.getPositions() != null ?
                portfolioDTO.getPositions().stream().map(
                i -> positionRepository.findOneByIdx(i.getIdx()).get()
        ).collect(Collectors.toSet()) : null;

        Set<TechVO> techVOS = (portfolioDTO.getTech() != null ?
        portfolioDTO.getTech().stream().map(
                i -> TechVO.builder()
                        .content(i.getContent())
                        .ability(i.getAbility())
                        .stack(stackRepository.getById(i.getStackIdx()))
                        .build()
        ).collect(Collectors.toSet()) : null);

        Optional<EducationVO> education = educationRepository.findById(portfolioDTO.getEducation().getIdx());

        PortfolioVO portfolioVO = PortfolioVO.builder()
                .user(uvo.get())
                .title(portfolioDTO.getTitle())
                .content(portfolioDTO.getContent())
                .project(projectSet)
                .tech(techVOS)
                .position(positionVOS)
                .education(education.get())
                .build();

        portfolioRepository.save(portfolioVO);
        return portfolioVO;
    }

    public PortfolioVO updatePortfolio(PortfolioDTO portfolioDTO){

        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        Set<ProjectVO> projectSet = portfolioDTO.getProject().stream().map(
                i -> ProjectVO.builder()
                        .title(i.getTitle())
                        .content(i.getContent())
                        .stack(i.getStack() != null ?
                                i.getStack().stream().map(
                                        t -> stackRepository.findOneByIdx(t.getIdx()).get()
                                ).collect(Collectors.toSet())
                                : null)
                        .build()
        ).collect(Collectors.toSet());

        Set<PositionVO> positionVOS =portfolioDTO.getPositions() != null ?
                portfolioDTO.getPositions().stream().map(
                        i -> positionRepository.findOneByIdx(i.getIdx()).get()
                ).collect(Collectors.toSet()) : null;

        Set<TechVO> techVOS = (portfolioDTO.getTech() != null ?
                portfolioDTO.getTech().stream().map(
                        i -> TechVO.builder()
                                .content(i.getContent())
                                .ability(i.getAbility())
                                .stack(stackRepository.getById(i.getStackIdx()))
                                .build()
                ).collect(Collectors.toSet()) : null);

        Optional<EducationVO> education = educationRepository.findById(portfolioDTO.getEducation().getIdx());

        PortfolioVO portfolioVO = PortfolioVO.builder()
                .idx(portfolioDTO.getIdx())
                .user(uvo.get())
                .title(portfolioDTO.getTitle())
                .content(portfolioDTO.getContent())
                .project(projectSet)
                .tech(techVOS)
                .position(positionVOS)
                .education(education.get())
                .build();

        portfolioRepository.save(portfolioVO);

        return portfolioVO;
    }

    public List<PortfolioListDTO> getMyPortfolios(){
        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(uvo.isPresent()){
            Optional<List<PortfolioVO>> portfolioList = portfolioRepository.findByUserUid(uvo.get().getUid());
            return portfolioList.get()
                    .stream()
                    .map(i -> {
                        List<String> stacks = new ArrayList<>();
                        for(TechVO vo : i.getTech()) stacks.add(vo.getStack().getName());

                        return PortfolioListDTO.builder()
                                .title(i.getTitle())
                                .content(i.getContent())
                                .reg_date(i.getRegDate())
                                .idx(i.getIdx())
                                .position(i.getPosition().stream().map(
                                        k -> PositionDTO.builder()
                                                .idx(k.getIdx())
                                                .name(k.getName())
                                                .build()
                                ).collect(Collectors.toList()))
                                .stack(stacks.stream().map(
                                        p -> StackDTO.builder()
                                                .name(p)
                                                .build()
                                ).collect(Collectors.toList()))
                                .education(EducationDTO.builder()
                                        .name(i.getEducation().getName())
                                        .idx(i.getEducation().getIdx())
                                        .build())
                                .build();

                    }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
