package com.limbae.pfy.controller;


import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.dto.PortfolioDTO;
import com.limbae.pfy.dto.PortfolioListDto;
import com.limbae.pfy.service.PortfolioService;
import com.limbae.pfy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserService userService;

    @Autowired
    PortfolioService portfolioService;

    @GetMapping("/portfolios")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PortfolioListDto>> getPortfolioList() {
        return ResponseEntity.ok(userService.getMyPortfolios());
    }



    @GetMapping("/portfolio")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> getMyPortfolioByIdx(@RequestParam(value = "portfolio_idx") int idx){
        List<PortfolioListDto> pfs = userService.getMyPortfolios();

        Optional<PortfolioVO> opvo = portfolioService.getPortfolioByIdx(idx);
        if(opvo.isEmpty()) return ResponseEntity.badRequest().build();
        PortfolioVO getvo = opvo.get();

        PortfolioListDto any = null;

        for(PortfolioListDto dto : pfs)
            if(dto.getIdx() == getvo.getIdx()) any = dto;


        if(any == null) return ResponseEntity.badRequest().build();
        //위까지 idx 포트폴리오가 토큰유저 소유 포트폴리오인지 검사

        PortfolioDTO pdto = PortfolioDTO.builder()
                .idx(getvo.getIdx())
                .content(getvo.getContent())
                .positions(getvo.getPosition())
                .project(getvo.getProject())
                .title(getvo.getTitle())
                .regDate(getvo.getRegDate())
                .build();

        return ResponseEntity.ok(pdto);
    }

    @PostMapping("/portfolio")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> addPortfolio(
            @RequestBody PortfolioDTO portfolioDTO){

        return null;
    }

}
