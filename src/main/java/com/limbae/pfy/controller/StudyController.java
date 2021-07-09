package com.limbae.pfy.controller;


import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    public StudyController(UserService userService, PortfolioService portfolioService,
                           EntityUtil entityUtil, StackService stackService,
                           PositionService positionService, StudyService studyService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.studyService = studyService;
    }


    @GetMapping("/studies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyDTO>> getMyStudyList() {
        return ResponseEntity.ok(studyService.getMyStudyList().stream().map(
                i -> entityUtil.convertStudyVoToDto(i)
        ).collect(Collectors.toList()));
    }

    @PostMapping("/study")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyDTO> saveStudy(
            @RequestBody StudyDTO studyDTO){
        return ResponseEntity.ok(entityUtil.convertStudyVoToDto(studyService.saveStudy(studyDTO)));
    }

    @GetMapping("/study/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @RequestParam(name = "studyIdx") Long studyIdx){
        return ResponseEntity.ok(studyService.getAnnouncementListByStudyIdx(studyIdx).get().stream().map(
                i -> entityUtil.convertAnnouncementVoToDto(i)
        ).collect(Collectors.toList()));
    }

    @PostMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO){
        return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(studyService.saveAnnouncement(announcementDTO)));
    }
}
