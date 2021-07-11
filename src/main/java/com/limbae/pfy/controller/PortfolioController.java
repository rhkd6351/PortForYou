package com.limbae.pfy.controller;


import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.EducationRepository;
import com.limbae.pfy.service.PortfolioService;
import com.limbae.pfy.service.PositionService;
import com.limbae.pfy.service.StackService;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api/user"})
@Slf4j
public class PortfolioController {

    UserService userService;
    PortfolioService portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    EducationRepository educationRepository;

    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               EntityUtil entityUtil, StackService stackService,
                               PositionService positionService,
                               EducationRepository educationRepository) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.educationRepository = educationRepository;
    }

    @GetMapping("/portfolios")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PortfolioListDTO>> getPortfolioList() {
        return ResponseEntity.ok(portfolioService.getMyPortfolios());
    }



    @GetMapping("/portfolio")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> getMyPortfolioByIdx(@RequestParam(value = "portfolio_idx") int idx){

        Optional<PortfolioVO> opvo = portfolioService.getPortfolioByIdx(idx);

        if(opvo.isEmpty()) return ResponseEntity.badRequest().build();
        PortfolioVO getvo = opvo.get();

        if(getvo.getUser() != userService.getMyUserWithAuthorities().get())
            return ResponseEntity.badRequest().build();
        //위까지 idx 포트폴리오가 토큰유저 소유 포트폴리오인지 검사

        return ResponseEntity.ok(entityUtil.convertPortfolioVoToDto(getvo));
    }

    @PostMapping("/portfolio")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> savePortfolio(
            @RequestBody PortfolioDTO portfolioDTO){
        try{
            PortfolioVO pvo = portfolioService.savePortfolio(portfolioDTO);
            return ResponseEntity.ok(entityUtil.convertPortfolioVoToDto(pvo));
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping("/portfolio")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @RequestBody PortfolioDTO portfolioDTO){
        PortfolioVO pvo = portfolioService.updatePortfolio(portfolioDTO);

        return ResponseEntity.ok(entityUtil.convertPortfolioVoToDto(pvo));
    }

    @GetMapping("/portfolio/stacks")
    public ResponseEntity<List<StackDTO>> getStackList() {
        List<StackDTO> stackList = stackService.getStackList().stream().map(
                entityUtil::convertStackVoToDto
        ).collect(Collectors.toList());

        return ResponseEntity.ok(stackList);
    }

    @GetMapping("/portfolio/positions")
    public ResponseEntity<List<PositionDTO>> getPositionList() {
        List<PositionDTO> positionList = positionService.getPositionList().stream().map(
                entityUtil::convertPositionVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(positionList);
    }

    @GetMapping("/portfolio/educations")
    public ResponseEntity<List<EducationDTO>> getEducationList() {
        List<EducationDTO> educationList = educationRepository.findAll().stream().map(
                entityUtil::convertEducationVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(educationList);
    }


}
