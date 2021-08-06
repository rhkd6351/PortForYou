package com.limbae.pfy.controller.user;


import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.etc.ImageVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.EducationRepository;
import com.limbae.pfy.service.etc.ImageService;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.etc.StackService;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api/user"})
@Slf4j
public class PortfolioController {

    UserServiceInterfaceImpl userService;
    PortfolioServiceInterfaceImpl portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    EducationRepository educationRepository;
    ImageService imageService;
    private final String serverUri;

    public PortfolioController(UserServiceInterfaceImpl userService, PortfolioServiceInterfaceImpl portfolioService,
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
            throws Exception {
        UserVO user = userService.getByAuth();
        List<PortfolioVO> portfolios = portfolioService.getByUid(user.getUid());
        List<PortfolioDTO> portfoliosDTO = portfolios.stream().
                map(entityUtil::convertPortfolioVoToDto).collect(Collectors.toList());
        //response
        return ResponseEntity.ok(portfoliosDTO);
    }

    @GetMapping("/portfolio/{portfolio-idx}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> getMyPortfolioByIdx(
            @PathVariable(value = "portfolio-idx") Long idx) throws NotFoundException, AuthException {

        UserVO user = userService.getByAuth();

        PortfolioVO portfolio = portfolioService.getByIdx(idx);

        //소유권 확인
        if(portfolio.getUser() != user)
            throw new AuthException("not owned portfolio");

        PortfolioDTO portfolioDTO = entityUtil.convertPortfolioVoToDto(portfolio);

        //이미지 주소 연결
        try{
            ImageVO image = imageService.getByName(portfolioDTO.getIdx() + "_portfolio_img");
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
            @PathVariable(value = "portfolio-idx") Long idx) throws Exception {

        UserVO user = userService.getByAuth();

        PortfolioVO portfolio = portfolioService.getByIdx(idx);

        //소유권 확인
        if(portfolio.getUser() != user)
            throw new AuthException("not owned portfolio");

        portfolioService.delete(portfolio);

        return new ResponseEntity<ResponseObjectDTO>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/portfolio", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @RequestBody PortfolioDTO portfolioDTO) throws NotFoundException, AuthException {

        PortfolioVO pvo = portfolioService.update(portfolioDTO);

        return new ResponseEntity<PortfolioDTO>(entityUtil.convertPortfolioVoToDto(pvo),HttpStatus.CREATED);
    }


}
