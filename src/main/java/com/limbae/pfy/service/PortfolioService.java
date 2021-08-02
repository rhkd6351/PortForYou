package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.*;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PortfolioService implements PortfolioServiceInterface {

    public PortfolioService(PortfolioRepository portfolioRepository, PositionRepository positionRepository, UserRepository userRepository, StackRepository stackRepository, EducationRepository educationRepository, ProjectRepository projectRepository, TechRepository techRepository) {
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;
        this.stackRepository = stackRepository;
        this.educationRepository = educationRepository;
        this.projectRepository = projectRepository;
        this.techRepository = techRepository;
    }

    PortfolioRepository portfolioRepository;
    PositionRepository positionRepository;
    UserRepository userRepository;
    StackRepository stackRepository;
    EducationRepository educationRepository;
    ProjectRepository projectRepository;
    TechRepository techRepository;


    public PortfolioVO getPortfolioByIdx(Long idx) throws NotFoundException {
        Optional<PortfolioVO> portfolio = portfolioRepository.findOneByIdx(idx);

        if(portfolio.isPresent())
            return portfolio.get();
        else
            throw new NotFoundException("invalid portfolio idx");
    }

    public void deletePortfolio(PortfolioVO vo){
        portfolioRepository.delete(vo);
    }

    public PortfolioVO updatePortfolio(PortfolioDTO portfolioDTO) throws NotFoundException, AuthException {

        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(uvo.isEmpty()) throw new AuthException("Invalid Token");

        Optional<EducationVO> education = educationRepository.findById(portfolioDTO.getEducation().getIdx());
        if(education.isEmpty()) throw new NotFoundException("Invalid Education Idx");

        Optional<PositionVO> position = positionRepository.findOneByIdx(portfolioDTO.getPosition().getIdx());
        if(position.isEmpty()) throw new NotFoundException("Invalid Position Idx");

        // 프로젝트, 테크 변경전 데이터랑 변경후 데이터 비교해서 삭제된 데이터 삭제쿼리 날리기
        // -> entity의 column 속성으로 orphanRemoval(고아 객체 제거) 추가하여 위 기능 구현

        List<ProjectVO> projectList = portfolioDTO.getProject() != null ?
                portfolioDTO.getProject().stream().map(
                        i -> ProjectVO.builder()
                                .idx(i.getIdx())
                                .title(i.getTitle())
                                .content(i.getContent())
                                .stack(i.getStack() != null ?
                                        i.getStack().stream().map(
                                                t -> stackRepository.findOneByIdx(t.getIdx()).orElse(null)
                                        ).collect(Collectors.toList())
                                        : null)
                                .build()
                ).collect(Collectors.toList()) : null;
        projectRepository.saveAll(projectList);



        Set<TechVO> techVOS = (portfolioDTO.getTech() != null ?
                portfolioDTO.getTech().stream().map(
                        i -> TechVO.builder()
                                .idx(i.getIdx())
                                .content(i.getContent())
                                .ability(i.getAbility())
                                .stack(stackRepository.getById(i.getStackIdx()))
                                .build()
                ).collect(Collectors.toSet()) : null);
        techRepository.saveAll(techVOS);


        if(portfolioDTO.getIdx() == null){
            PortfolioVO portfolioVO = PortfolioVO.builder()
                    .user(uvo.get())
                    .title(portfolioDTO.getTitle())
                    .content(portfolioDTO.getContent())
                    .project(projectList)
                    .tech(techVOS)
                    .position(position.get())
                    .education(education.get())
                    .build();

            portfolioRepository.save(portfolioVO);
            return portfolioVO;
        }

        PortfolioVO portfolioVO = portfolioRepository.getById(portfolioDTO.getIdx());
        portfolioVO.setTitle(portfolioDTO.getTitle());
        portfolioVO.setContent(portfolioDTO.getContent());
        portfolioVO.setProject(projectList);
        portfolioVO.setTech(techVOS);
        portfolioVO.setPosition(position.get());
        portfolioVO.setEducation(education.get());

        portfolioRepository.save(portfolioVO);

        return portfolioVO;
    }

    public List<PortfolioVO> getPortfoliosByUid(Long uid) {
        Optional<List<PortfolioVO>> portfolios = portfolioRepository.findByUserUid(uid);
        return portfolios.orElse(new ArrayList<PortfolioVO>());
    }


}
