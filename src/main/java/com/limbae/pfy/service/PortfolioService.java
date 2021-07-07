package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.PortfolioDTO;
import com.limbae.pfy.dto.PortfolioListDto;
import com.limbae.pfy.dto.PositionDTO;
import com.limbae.pfy.dto.StackDTO;
import com.limbae.pfy.repository.PortfolioRepository;
import com.limbae.pfy.repository.PositionRepository;
import com.limbae.pfy.repository.StackRepository;
import com.limbae.pfy.repository.UserRepository;
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

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, PositionRepository positionRepository, UserRepository userRepository, StackRepository stackRepository) {
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.stackRepository = stackRepository;
    }

    public Optional<PortfolioVO> getPortfolioByIdx(int idx){
        return portfolioRepository.findOneWithProjectAndPositionByIdx(idx);
    }

    public boolean checkPossessionOfPortfolio(PortfolioVO pfvo,
                                              List<PortfolioListDto> pfList){
        PortfolioListDto any = null;


        for(PortfolioListDto dto : pfList)
            if(dto.getIdx() == pfvo.getIdx()) any = dto;

        return any != null;
    }

    public PortfolioVO savePortfolio(Long user_uid, PortfolioDTO portfolioDTO){

        Set<ProjectVO> projectSet = portfolioDTO.getProject().stream().map(
                i -> ProjectVO.builder()
                        .title(i.getTitle())
                        .content(i.getContent())
                        .stack(
                                i.getStack().stream().map(
                                        t -> stackRepository.findOneByIdx(t.getIdx()).get()
                                ).collect(Collectors.toSet())
                        )
                        .build()
        ).collect(Collectors.toSet());



        Set<PositionVO> positionVOS = portfolioDTO.getPositions().stream().map(
                i -> positionRepository.findOneByIdx(i.getIdx()).get()
        ).collect(Collectors.toSet());

        Set<TechVO> techVOS = portfolioDTO.getTech().stream().map(
                i -> TechVO.builder()
                        .content(i.getContent())
                        .ability(i.getAbility())
                        .stack(stackRepository.getById(i.getStackIdx()))
                        .build()
        ).collect(Collectors.toSet());

        PortfolioVO portfolioVO = PortfolioVO.builder()
                .user(userRepository.getById(user_uid))
                .title(portfolioDTO.getTitle())
                .content(portfolioDTO.getContent())
                .project(projectSet)
                .tech(techVOS)
                .position(positionVOS)
                .build();

        portfolioRepository.save(portfolioVO);
        return portfolioVO;
    }

    public PortfolioVO updatePortfolio(Long user_uid, PortfolioDTO portfolioDTO){

        Set<ProjectVO> projectSet = portfolioDTO.getProject().stream().map(
                i -> ProjectVO.builder()
                        .title(i.getTitle())
                        .content(i.getContent())
                        .stack(
                                i.getStack().stream().map(
                                        t -> stackRepository.findOneByIdx(t.getIdx()).get()
                                ).collect(Collectors.toSet())
                        )
                        .build()
        ).collect(Collectors.toSet());



        Set<PositionVO> positionVOS = portfolioDTO.getPositions().stream().map(
                i -> positionRepository.findOneByIdx(i.getIdx()).get()
        ).collect(Collectors.toSet());

        Set<TechVO> techVOS = portfolioDTO.getTech().stream().map(
                i -> TechVO.builder()
                        .content(i.getContent())
                        .ability(i.getAbility())
                        .stack(stackRepository.getById(i.getStackIdx()))
                        .build()
        ).collect(Collectors.toSet());

        PortfolioVO portfolioVO = PortfolioVO.builder()
                .idx(portfolioDTO.getIdx())
                .user(userRepository.getById(user_uid))
                .title(portfolioDTO.getTitle())
                .content(portfolioDTO.getContent())
                .project(projectSet)
                .tech(techVOS)
                .position(positionVOS)
                .build();

        portfolioRepository.save(portfolioVO);

        return portfolioVO;
    }

    public List<PortfolioListDto> getMyPortfolios(){
        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithPortfolioByUsername);
        if(uvo.isPresent()){
            return uvo.get().getPortfolio()
                    .stream()
                    .map(i -> {
                        List<String> stacks = new ArrayList<>();
                        for(ProjectVO vo : i.getProject()){
                            Set<StackVO> stack = vo.getStack();
                            for(StackVO stackVO : stack){
                                if(!stacks.contains(stackVO.getName()))
                                    stacks.add(stackVO.getName());
                            }
                        }
                        return PortfolioListDto.builder()
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
                                .build();
                    }).collect(Collectors.toList());
        }else{
            return null;
        }
    }
}
