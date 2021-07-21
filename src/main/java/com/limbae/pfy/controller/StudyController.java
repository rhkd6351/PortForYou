package com.limbae.pfy.controller;


import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.StudyApplicationVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.ApplyAnnouncementDTO;
import com.limbae.pfy.dto.StudyApplicationDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.repository.StudyApplicationRepository;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
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
public class StudyController {

    UserService userService;
    PortfolioService portfolioService;
    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    StudyService studyService;
    StudyApplicationService studyApplicationService;

    public StudyController(UserService userService, PortfolioService portfolioService,
                           EntityUtil entityUtil, StackService stackService,
                           PositionService positionService, StudyService studyService,
                           StudyApplicationService studyApplicationService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.studyService = studyService;
        this.studyApplicationService = studyApplicationService;
    }


    @GetMapping("/studies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyDTO>> getMyStudyList(@RequestParam(required = false) boolean applied) {
        if(applied){
            return ResponseEntity.ok(studyService.getMyAppliedStudyList().stream().map(
                    i -> {
                        StudyDTO studyDTO = entityUtil.convertStudyVoToDto(i);
                        studyDTO.setMembers(i.getMembers().size());
                        return studyDTO;
                    }
            ).collect(Collectors.toList()));
        }
        return studyService.getMyStudyList() != null ?
                ResponseEntity.ok(studyService.getMyStudyList().stream().map(
                        i -> {
                            StudyDTO studyDTO = entityUtil.convertStudyVoToDto(i);
                            studyDTO.setMembers(i.getMembers().size());
                            return studyDTO;
                        }
                ).collect(Collectors.toList())) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/study")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyDTO> getMyStudyByIdx(@RequestParam(name = "studyIdx") Long studyIdx) {
        StudyVO studyByIdx = studyService.getStudyByIdx(studyIdx);

        if(studyByIdx == null) //NOT FOUND
            return new ResponseEntity<StudyDTO>(HttpStatus.NOT_FOUND);

        if((studyByIdx.getUser() != userService.getMyUserWithAuthorities().get())) //UNAUTHORIZED
            return new ResponseEntity<StudyDTO>(HttpStatus.UNAUTHORIZED);

        return ResponseEntity.ok(entityUtil.convertStudyVoToDto(studyByIdx));
    }

    @PostMapping("/study")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyDTO> saveStudy(
            @RequestBody StudyDTO studyDTO) {
        StudyVO studyVO = null;
        try {
            studyVO = studyService.saveStudy(studyDTO);
        } catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(entityUtil.convertStudyVoToDto(studyVO));
    }

    @GetMapping("/study/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @RequestParam(name = "studyIdx") Long studyIdx) {
        Optional<List<AnnouncementVO>> announcementListByStudyIdx = studyService.getAnnouncementListByStudyIdx(studyIdx);

        if(announcementListByStudyIdx.isEmpty()) return ResponseEntity.badRequest().build();

        return announcementListByStudyIdx.map(announcementVOS -> ResponseEntity.ok(announcementVOS.stream().map(
                i -> entityUtil.convertAnnouncementVoToDto(i)
        ).collect(Collectors.toList()))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO) {
        AnnouncementVO announcementVO = studyService.saveAnnouncement(announcementDTO);

        if(announcementVO == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(announcementVO));
    }

    @PostMapping("/study/announcement/application")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyApplicationDTO> applyPortfolioToAnnouncement(
            @RequestBody StudyApplicationDTO dto){
        StudyApplicationDTO studyApplicationDTO = entityUtil.convertStudyApplicationVoToDto(
                studyApplicationService.saveStudyApplication(dto));
        if(studyApplicationDTO == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(studyApplicationDTO);
    }

    @GetMapping("/study/announcement/applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyApplicationDTO>> getStudyApplicationListByStudyIdx(
            @RequestParam(name = "studyIdx") Long studyIdx){
        List<StudyApplicationVO> saVO = studyApplicationService.getStudyApplicationListByStudyIdx(studyIdx);
        if(saVO == null)
            return ResponseEntity.badRequest().build();

        List<StudyApplicationDTO> collect = saVO.stream().map(
                i -> entityUtil.convertStudyApplicationVoToDto(i)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }
}










