package com.limbae.pfy.controller;


import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.io.NotActiveException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api"})
@Slf4j
public class ApplicationController {

    UserServiceInterface userServiceInterface;
    PortfolioService portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    StudyService studyService;
    StudyApplicationService studyApplicationService;
    AnnouncementService announcementService;
    MemberService memberService;

    public ApplicationController(UserServiceInterface userServiceInterface, PortfolioService portfolioService, EntityUtil entityUtil, StackService stackService, PositionService positionService, StudyService studyService, StudyApplicationService studyApplicationService, AnnouncementService announcementService, MemberService memberService) {
        this.userServiceInterface = userServiceInterface;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.studyService = studyService;
        this.studyApplicationService = studyApplicationService;
        this.announcementService = announcementService;
        this.memberService = memberService;
    }

    @GetMapping("/study/{study-idx}/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getStudyApplicationListByStudyIdx(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        //Object user = Hibernate.unproxy(saVO.get(0).getPortfolio().getUser());
        //Hibernate unproxy..? proxy로 묶여버린 entity 풀어주는 작업.. 이게 왜 필요하지
        //근데 proxy 안해도 갑자기 들어감..뭐지

        //it can throw NotFoundException
        StudyVO study = studyService.getStudyByIdx(studyIdx);

        //it can throw AuthException
        UserVO user = userServiceInterface.getMyUserWithAuthorities();

        if(user != study.getUser())
            throw new AuthException("not owned study");

        List<StudyApplicationVO> studyApplications = studyApplicationService.getStudyApplicationListByStudyIdx(studyIdx);

        List<StudyApplicationDTO> studyApplicationDTOS = studyApplications.stream()
                .map(entityUtil::convertStudyApplicationVoToDto)
                .collect(Collectors.toList());

        //dto.portfolio.tech에 vo.portfolio.tech 주입
        for(StudyApplicationDTO dto : studyApplicationDTOS)
            for(TechDTO techDTO : dto.getPortfolio().getTech())
                techDTO.setStackName(stackService.getStackByIdx(techDTO.getStackIdx()).getName());

        return ResponseEntity.ok(studyApplicationDTOS);
    }

    @GetMapping("/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getMyApplicationList() throws AuthException {

        //it can throw AuthException
        UserVO user = userServiceInterface.getMyUserWithAuthorities();

        List<StudyApplicationVO> applicationLIstByUid = studyApplicationService.getStudyApplicationListByUid(user.getUid());

        List<StudyApplicationDTO> applicationDTOS = applicationLIstByUid.stream()
                .map(entityUtil::convertStudyApplicationVoToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(applicationDTOS);
    }

    @PostMapping("/announcement/{announcement-idx}/application")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyApplicationDTO> applyPortfolioToAnnouncement(
            @RequestBody StudyApplicationDTO dto,
            @PathVariable(value = "announcement-idx")Long announcementIdx) throws Exception{

        UserVO user = userServiceInterface.getMyUserWithAuthorities();
        AnnouncementVO announcement = announcementService.getAnnouncementByIdx(announcementIdx);
        PositionVO position = positionService.getPositionByIdx(dto.getPosition().getIdx());
        PortfolioVO portfolio = portfolioService.getPortfolioByIdx(dto.getPortfolio().getIdx());

        if(user != portfolio.getUser())
            throw new AuthException("not owned portfolio");

        if(!announcement.isActivated())
            throw new NotActiveException("announcement is closed");

        //vo 변환 서비스로 분리? 아니면 그대로? 다른 서비스에선 dto 받아서 vo로 변환해주는 작업 진행하였음
        StudyApplicationVO studyApplication = StudyApplicationVO.builder()
                .announcement(announcement)
                .portfolio(portfolio)
                .position(position)
                .declined(0L)
                .build();

        studyApplicationService.saveStudyApplication(studyApplication);
        return new ResponseEntity<>(entityUtil.convertStudyApplicationVoToDto(studyApplication),HttpStatus.CREATED);
    }

    @GetMapping("/application/{application-idx}/accept")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> acceptMember(
            @PathVariable("application-idx") Long applicationIdx) throws AuthException, NotFoundException {

        StudyApplicationVO studyApplication = studyApplicationService.getStudyApplicationByIdx(applicationIdx);
        UserVO user = userServiceInterface.getMyUserWithAuthorities();

        if(studyApplication.getAnnouncement().getStudy().getUser() != user)
            throw new AuthException("not owned study");

        studyApplicationService.acceptApplication(studyApplication);

        return new ResponseEntity<>(new ResponseObjectDTO("accept success"), HttpStatus.OK);
    }

    @GetMapping("/application/{application-idx}/decline")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> declineMember(
            @PathVariable("application-idx") Long applicationIdx) throws AuthException, NotFoundException {
        StudyApplicationVO studyApplication = studyApplicationService.getStudyApplicationByIdx(applicationIdx);
        UserVO user = userServiceInterface.getMyUserWithAuthorities();

        if(studyApplication.getAnnouncement().getStudy().getUser() != user)
            throw new AuthException("not owned study");

        studyApplicationService.declineApplication(studyApplication);

        return new ResponseEntity<>(new ResponseObjectDTO("decline success"), HttpStatus.OK);
    }
}










