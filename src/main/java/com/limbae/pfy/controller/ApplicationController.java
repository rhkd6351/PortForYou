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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api/user"})
@Slf4j
public class ApplicationController {

    UserService userService;
    PortfolioService portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    StudyService studyService;
    StudyApplicationService studyApplicationService;
    AnnouncementService announcementService;
    StudyCategoryService studyCategoryService;
    MemberService memberService;


    @Autowired
    public ApplicationController(UserService userService, PortfolioService portfolioService,
                                 EntityUtil entityUtil, StackService stackService,
                                 MemberService memberService,
                                 PositionService positionService, StudyService studyService,
                                 StudyApplicationService studyApplicationService,
                                 AnnouncementService announcementService, StudyCategoryService studyCategoryService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.studyService = studyService;
        this.studyApplicationService = studyApplicationService;
        this.announcementService = announcementService;
        this.studyCategoryService = studyCategoryService;
        this.memberService = memberService;
    }

    @PostMapping("/study/announcement/application")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyApplicationDTO> applyPortfolioToAnnouncement(
            @RequestBody StudyApplicationDTO dto){

        Optional<PortfolioVO> portfolioByIdx = portfolioService.getPortfolioByIdx(dto.getPortfolio().getIdx());
        AnnouncementVO announcementByIdx = announcementService.getAnnouncementByIdx(dto.getAnnouncement().getIdx());
        Optional<PositionVO> positionByIdx = positionService.getPositionByIdx(dto.getPosition().getIdx());

        if(portfolioByIdx.isEmpty() || announcementByIdx == null || positionByIdx.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); //객체 존재 확인

        if(userService.getMyUserWithAuthorities().get() != portfolioByIdx.get().getUser())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //포트폴리오 소유 확인

        if(!announcementByIdx.isActivated())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //공문 마감

        StudyApplicationDTO studyApplicationDTO = null;
        studyApplicationDTO = entityUtil.convertStudyApplicationVoToDto(
                studyApplicationService.saveStudyApplication(dto));

        if(studyApplicationDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(studyApplicationDTO);
    }

    @GetMapping("/study/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getStudyApplicationListByStudyIdx(
            @RequestParam(name = "studyIdx") Long studyIdx){

        StudyVO studyByIdx = studyService.getStudyByIdx(studyIdx);

        if(studyByIdx == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(userService.getMyUserWithAuthorities().get() != studyService.getStudyByIdx(studyIdx).getUser())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //스터디 자기소유 확인

        List<StudyApplicationVO> saVO = studyApplicationService.getStudyApplicationListByStudyIdx(studyIdx);

        //Object user = Hibernate.unproxy(saVO.get(0).getPortfolio().getUser());
        //Hibernate unproxy..? proxy로 묶여버린 entity 풀어주는 작업.. 이게 왜 필요하지
        //근데 proxy 안해도 갑자기 들어감..뭐지

        if(saVO.size() == 0)
            return ResponseEntity.ok(null);

        List<StudyApplicationDTO> collect = saVO.stream().map(
                i -> entityUtil.convertStudyApplicationVoToDto(i)
        ).collect(Collectors.toList());


        for(StudyApplicationDTO dto :collect)
            for(TechDTO techDTO : dto.getPortfolio().getTech())
                techDTO.setStackName(stackService.getStackByIdx(techDTO.getStackIdx()).getName());

        return ResponseEntity.ok(collect);
    }

    @GetMapping("/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getMyApplicationList(){
        UserVO userVO = userService.getMyUserWithAuthorities().get();
        List<StudyApplicationVO> applicationLIstByUid = studyApplicationService.getStudyApplicationLIstByUid(userVO.getUid());

        List<StudyApplicationDTO> applicationDTOS = applicationLIstByUid.stream().map(
                i -> entityUtil.convertStudyApplicationVoToDto(i)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(applicationDTOS);
    }

    @PostMapping("/study/member/application")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> acceptMember(
            @RequestParam("applicationIdx") Long applicationIdx) {
        Optional<StudyApplicationVO> studyApplicationByIdx = studyApplicationService.getStudyApplicationByIdx(applicationIdx);

        if(studyApplicationByIdx.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(studyApplicationByIdx.get().getAnnouncement().getStudy().getUser() != userService.getMyUserWithAuthorities().get())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        StudyApplicationVO studyApplicationVO = studyApplicationByIdx.get();

        if(studyApplicationVO.getDeclined() != 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        studyApplicationVO.setDeclined(-1L); // -1 : Accepted

        for(DemandPositionVO vo :studyApplicationVO.getAnnouncement().getDemandPosition()) {
            if(vo.getPosition() == studyApplicationVO.getPosition()){
                vo.setApplied(vo.getApplied() + 1); // applied += 1
                break;
            }
        }
        UserVO user = studyApplicationVO.getPortfolio().getUser();
        MemberVO member = MemberVO.builder()
                .user(user)
                .position(studyApplicationVO.getPosition())
                .study(studyApplicationVO.getAnnouncement().getStudy())
                .build();

        memberService.saveMember(member);
        studyApplicationService.saveStudyApplication(studyApplicationVO);

        return new ResponseEntity<>(new ResponseObjectDTO("accept success"), HttpStatus.OK);
    }

    @DeleteMapping("/study/member/application")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> declineMember(
            @RequestParam("applicationIdx") Long applicationIdx) {
        Optional<StudyApplicationVO> studyApplicationByIdx = studyApplicationService.getStudyApplicationByIdx(applicationIdx);

        if(studyApplicationByIdx.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(studyApplicationByIdx.get().getAnnouncement().getStudy().getUser() != userService.getMyUserWithAuthorities().get())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        StudyApplicationVO studyApplicationVO = studyApplicationByIdx.get();

        if(studyApplicationVO.getDeclined() != 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        studyApplicationVO.setDeclined(1L); // -1 : Accepted

        studyApplicationService.saveStudyApplication(studyApplicationVO);

        return new ResponseEntity<>(new ResponseObjectDTO("decline success"), HttpStatus.OK);
    }


}










