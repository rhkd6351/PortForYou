package com.limbae.pfy.controller;


import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.ImageVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.EducationRepository;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
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
    ImageService imageService;
    private final String serverUri;

    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               EntityUtil entityUtil, StackService stackService,
                               PositionService positionService,
                               EducationRepository educationRepository, ImageService imageService,
                               @Value(value = "${server.uri}") String serverUri) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.educationRepository = educationRepository;
        this.imageService = imageService;
        this.serverUri = serverUri;
    }

    @GetMapping("/portfolios")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PortfolioDTO>> getPortfolioList()
            throws AuthException {
        //it can throw AuthException
        UserVO user = userService.getMyUserWithAuthorities();

        List<PortfolioVO> portfolios = portfolioService.getPortfoliosByUid(user.getUid());
        List<PortfolioDTO> portfoliosDTO = portfolios.stream().
                map(entityUtil::convertPortfolioVoToDto).collect(Collectors.toList());

        //response
        return ResponseEntity.ok(portfoliosDTO);
    }

    @GetMapping("/portfolio/{portfolio-idx}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> getMyPortfolioByIdx(
            @PathVariable(value = "portfolio-idx") Long idx) throws NotFoundException, AuthException {
        //it can throw AuthException
        UserVO user = userService.getMyUserWithAuthorities();

        //It can throw NotFoundException
        PortfolioVO portfolio = portfolioService.getPortfolioByIdx(idx);

        //소유권 확인
        if(portfolio.getUser() != user)
            throw new AuthException("not owned portfolio");

        PortfolioDTO portfolioDTO = entityUtil.convertPortfolioVoToDto(portfolio);

        //이미지 주소 연결
        try{
            ImageVO image = imageService.getImageWithName(portfolioDTO.getIdx() + "_portfolio_img");
            String uri = serverUri + "/api/img/default/" + image.getName();
            portfolioDTO.setImg(uri);
        }catch (NotFoundException e){
            portfolioDTO.setImg("not registered");
        }

        return ResponseEntity.ok(portfolioDTO);

    }

    @DeleteMapping("/portfolio/{portfolio-idx}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deletePortfolioByIdx(
            @PathVariable(value = "portfolio-idx") Long idx) throws NotFoundException, AuthException {

        //it can throw AuthException
        UserVO user = userService.getMyUserWithAuthorities();

        //It can throw NotFoundException
        PortfolioVO portfolio = portfolioService.getPortfolioByIdx(idx);

        //소유권 확인
        if(portfolio.getUser() != user)
            throw new AuthException("not owned portfolio");

        portfolioService.deletePortfolio(portfolio);

        //response 204 -> no content (delete success)
        return new ResponseEntity<ResponseObjectDTO>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/portfolio", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @RequestBody PortfolioDTO portfolioDTO) throws NotFoundException, AuthException {

        //it can throw AuthException, NotFoundException
        PortfolioVO pvo = portfolioService.updatePortfolio(portfolioDTO);

        return new ResponseEntity<PortfolioDTO>(entityUtil.convertPortfolioVoToDto(pvo),HttpStatus.CREATED);
    }


}
