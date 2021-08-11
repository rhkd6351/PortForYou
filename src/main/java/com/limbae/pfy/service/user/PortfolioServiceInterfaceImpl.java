package com.limbae.pfy.service.user;

import com.limbae.pfy.domain.etc.EducationVO;
import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.etc.ProjectVO;
import com.limbae.pfy.domain.etc.TechVO;
import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.user.PortfolioDTO;
import com.limbae.pfy.repository.user.PortfolioRepository;
import com.limbae.pfy.service.etc.EducationService;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.etc.ProjectService;
import com.limbae.pfy.service.etc.TechService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PortfolioServiceInterfaceImpl implements PortfolioServiceInterface {



    PortfolioRepository portfolioRepository;

    UserServiceInterface userService;
    EducationService educationService;
    PositionService positionService;
    ProjectService projectService;
    TechService techService;

    public PortfolioServiceInterfaceImpl(PortfolioRepository portfolioRepository, UserServiceInterface userService, EducationService educationService, PositionService positionService, ProjectService projectService, TechService techService) {
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
        this.educationService = educationService;
        this.positionService = positionService;
        this.projectService = projectService;
        this.techService = techService;
    }

    public PortfolioVO getByIdx(Long idx) throws NotFoundException {
        Optional<PortfolioVO> portfolio = portfolioRepository.findOneByIdx(idx);

        if(portfolio.isEmpty())
            throw new NotFoundException("invalid portfolio idx");

        return portfolio.get();
    }

    public List<PortfolioVO> getByUid(Long uid) throws Exception {
        UserVO user = userService.getByUid(uid);

        return portfolioRepository.findByUserUid(user.getUid());
    }

    public void delete(PortfolioVO vo) throws Exception{
        try{
            portfolioRepository.delete(vo);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    public PortfolioVO update(PortfolioDTO portfolioDTO) throws NotFoundException, AuthException {

        UserVO uvo = userService.getByAuth();
        EducationVO education = educationService.getByIdx(portfolioDTO.getEducation().getIdx());
        PositionVO position = positionService.getByIdx(portfolioDTO.getPosition().getIdx());

        List<ProjectVO> projects = portfolioDTO.getProject().stream().map(
                i -> {
                    try {
                        return projectService.update(i);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
        }).collect(Collectors.toList());

        Set<TechVO> techs = portfolioDTO.getTech().stream().map(
                i -> {
                    try{
                        return techService.update(i);
                    }catch (NotFoundException e){
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toSet());

        PortfolioVO portfolio = null;

        if(portfolioDTO.getIdx() == null){
            portfolio = PortfolioVO.builder()
                    .user(uvo)
                    .title(portfolioDTO.getTitle())
                    .content(portfolioDTO.getContent())
                    .project(projects)
                    .tech(techs)
                    .position(position)
                    .education(education)
                    .build();
        }else{
            portfolio = portfolioRepository.getById(portfolioDTO.getIdx());
            portfolio.setTitle(portfolioDTO.getTitle());
            portfolio.setContent(portfolioDTO.getContent());
            portfolio.setProject(projects);
            portfolio.setTech(techs);
            portfolio.setPosition(position);
            portfolio.setEducation(education);
        }

        return portfolioRepository.save(portfolio);
    }


}
