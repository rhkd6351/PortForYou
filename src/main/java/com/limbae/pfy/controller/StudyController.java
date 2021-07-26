package com.limbae.pfy.controller;


import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    AnnouncementService announcementService;
    StudyCategoryService studyCategoryService;

    public StudyController(UserService userService, PortfolioService portfolioService,
                           EntityUtil entityUtil, StackService stackService,
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
    }

    @GetMapping("/study/categories")
    public ResponseEntity<List<StudyCategoryDTO>> getCategoryList() {
        List<StudyCategoryVO> categoryList = studyCategoryService.getCategoryList();

        List<StudyCategoryDTO> studyCategoryDTOList = categoryList.stream().map(
                i -> entityUtil.convertStudyCategoryVoToDto(i)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(studyCategoryDTOList);
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
                            studyDTO.setMembers(i.getMembers().size() + 1);
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

    @DeleteMapping("/study")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deleteStudy(
            @RequestParam Long studyIdx
    ){
        StudyVO studyByIdx = studyService.getStudyByIdx(studyIdx);
        if(studyByIdx == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(studyByIdx.getUser() != userService.getMyUserWithAuthorities().get())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(studyService.deleteStudy(studyByIdx)){
            return ResponseEntity.ok(new ResponseObjectDTO("delete success"));
        }else{
            return ResponseEntity.badRequest().build();
        }

    }


    @GetMapping("/study/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @RequestParam(name = "studyIdx") Long studyIdx) {
        StudyVO studyWithAnnouncementsByIdx = studyService.getStudyWithAnnouncementsByIdx(studyIdx);

        if(studyWithAnnouncementsByIdx == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(studyWithAnnouncementsByIdx.getUser() != userService.getMyUserWithAuthorities().get())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(studyWithAnnouncementsByIdx.getAnnouncements().stream().map(
                i -> entityUtil.convertAnnouncementVoToDto(i)
        ).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @RequestParam(name = "kind") String kind){

        List<AnnouncementDTO> dto = null;

        if(kind.equals("new")){
            dto = announcementService.getAnnouncementOrderByDesc().stream().map(entityUtil::convertAnnouncementVoToDto)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@RequestParam(name = "announcementIdx") Long announcementIdx){
        AnnouncementVO announcementVO = announcementService.getAnnouncementByIdx(announcementIdx);

        if(announcementVO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(entityUtil.convertAnnouncementVoToDto(announcementVO),HttpStatus.OK);

    }

    @PostMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO) {
        AnnouncementVO announcementVO = null;
        try{
            announcementVO = studyService.saveAnnouncement(announcementDTO);
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        if(announcementVO == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(announcementVO));
    }

    @DeleteMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deleteAnnouncement(
            @RequestParam(value = "announcementIdx") Long announcementIdx){
        AnnouncementVO announcementVO = announcementService.getAnnouncementByIdx(announcementIdx);

        if(announcementVO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        //소유 announcement 확인
        if(announcementVO.getStudy().getUser() != userService.getMyUserWithAuthorities().get())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if(announcementService.deleteAnnouncement(announcementVO))
            return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.OK);
        else
            return ResponseEntity.badRequest().build();

    }

    @GetMapping("/study/members")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<MemberDTO>> getMembersByStudyIdx(
            @RequestParam("studyIdx") Long studyIdx){

        StudyVO studyByIdx = studyService.getStudyByIdx(studyIdx);
        if(studyByIdx == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<MemberVO> membersInfo = studyByIdx.getMembersInfo();
        UserVO manager = studyByIdx.getUser();

        UserVO loginUser = userService.getMyUserWithAuthorities().get();
        if(!(loginUser == manager || membersInfo.stream().map(MemberVO::getUser).collect(Collectors.toSet()).contains(loginUser)))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<MemberDTO> membersWithManager = new ArrayList<>();
        membersWithManager.add(MemberDTO.builder().user(entityUtil.convertUserVoToDto(manager)).build());
        membersWithManager.addAll(membersInfo.stream().map(i ->
                MemberDTO.builder()
                        .user(entityUtil.convertUserVoToDto(i.getUser()))
                        .position(entityUtil.convertPositionVoToDto(i.getPosition()))
                        .build()
        ).collect(Collectors.toList()));

        return new ResponseEntity<>(membersWithManager, HttpStatus.OK);

    }
}










