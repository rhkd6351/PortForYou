package com.limbae.pfy.controller.study;


import com.limbae.pfy.domain.study.StudyApplicationVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.etc.ResponseObjectDTO;
import com.limbae.pfy.dto.etc.TechDTO;
import com.limbae.pfy.dto.study.AnnouncementDTO;
import com.limbae.pfy.dto.study.StudyApplicationDTO;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.etc.StackService;
import com.limbae.pfy.service.study.AnnouncementService;
import com.limbae.pfy.service.study.StudyApplicationService;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterface;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api"})
@Slf4j
public class ApplicationController {

    UserServiceInterface userService;
    PortfolioServiceInterfaceImpl portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    StudyServiceInterfaceImpl studyService;
    StudyApplicationService studyApplicationService;
    AnnouncementService announcementService;

    public ApplicationController(UserServiceInterface userService, PortfolioServiceInterfaceImpl portfolioService, EntityUtil entityUtil, StackService stackService, PositionService positionService, StudyServiceInterfaceImpl studyService, StudyApplicationService studyApplicationService, AnnouncementService announcementService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.studyService = studyService;
        this.studyApplicationService = studyApplicationService;
        this.announcementService = announcementService;
    }

    @GetMapping("/study/{study-idx}/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getStudyApplicationListByStudyIdx(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        StudyVO study = studyService.getByIdx(studyIdx);
        UserVO user = userService.getByAuth();

        if(user != study.getUser())
            throw new AuthException("not owned study");

        List<StudyApplicationVO> studyApplications = studyApplicationService.getByStudyIdx(studyIdx);

        List<StudyApplicationDTO> studyApplicationDTOS = studyApplications.stream()
                .map(entityUtil::convertStudyApplicationVoToDto)
                .collect(Collectors.toList());


        return ResponseEntity.ok(studyApplicationDTOS);
    }

    @GetMapping("/study/announcement/application/{application-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyApplicationDTO> getApplicationByIdx(
            @PathVariable(value = "application-idx") Long applicationIdx) throws NotFoundException, AuthException {
        StudyApplicationVO application = studyApplicationService.getByIdx(applicationIdx);
        UserVO user = userService.getByAuth();

        if(application.getPortfolio().getUser() != user){
            if(application.getAnnouncement().getStudy().getUser() != user)
                throw new AuthException("not owned study or not owned application");
        }


        return ResponseEntity.ok(entityUtil.convertStudyApplicationVoToDto(application));
    }

    @GetMapping("/user/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getMyApplicationList() throws Exception {

        UserVO user = userService.getByAuth();

        List<StudyApplicationVO> applicationLIstByUid = studyApplicationService.getByUid(user.getUid());

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

        dto.setAnnouncement(AnnouncementDTO.builder().idx(announcementIdx).build());

        StudyApplicationVO studyApplication = studyApplicationService.save(dto);

        return new ResponseEntity<>(entityUtil.convertStudyApplicationVoToDto(studyApplication),HttpStatus.CREATED);
    }

    @GetMapping("/application/{application-idx}/accept")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> acceptMember(
            @PathVariable("application-idx") Long applicationIdx) throws Exception {

        StudyApplicationVO studyApplication = studyApplicationService.getByIdx(applicationIdx);
        UserVO user = userService.getByAuth();

        if(studyApplication.getAnnouncement().getStudy().getUser() != user)
            throw new AuthException("not owned study");

        studyApplicationService.accept(studyApplication);

        return new ResponseEntity<>(new ResponseObjectDTO("accept success"), HttpStatus.OK);
    }

    @GetMapping("/application/{application-idx}/decline")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> declineMember(
            @PathVariable("application-idx") Long applicationIdx) throws Exception {
        StudyApplicationVO studyApplication = studyApplicationService.getByIdx(applicationIdx);
        UserVO user = userService.getByAuth();

        if(studyApplication.getAnnouncement().getStudy().getUser() != user)
            throw new AuthException("not owned study");

        studyApplicationService.decline(studyApplication);

        return new ResponseEntity<>(new ResponseObjectDTO("decline success"), HttpStatus.OK);
    }
}










