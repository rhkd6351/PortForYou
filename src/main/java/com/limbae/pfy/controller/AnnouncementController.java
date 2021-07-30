package com.limbae.pfy.controller;


import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.MemberVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.MemberDTO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.service.AnnouncementService;
import com.limbae.pfy.service.StudyService;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api"})
@Slf4j
public class AnnouncementController {

    UserService userService;
    EntityUtil entityUtil;
    StudyService studyService;
    AnnouncementService announcementService;

    public AnnouncementController(UserService userService, EntityUtil entityUtil, StudyService studyService, AnnouncementService announcementService) {
        this.userService = userService;
        this.entityUtil = entityUtil;
        this.studyService = studyService;
        this.announcementService = announcementService;
    }

    @GetMapping("/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList( //TODO kind required false로 바꾸고 default 쿼리 추가
            @RequestParam(name = "kind") String kind){

        List<AnnouncementDTO> dto = null;

        if(kind.equals("new")){
            dto = announcementService.getAnnouncementOrderByDesc().stream()
                    .map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList());
        }

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/study/{study-idx}/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        //it can throw Auth, NotFound Exception
        UserVO user = userService.getMyUserWithAuthorities();

        //it can throw NotFound Exception
        StudyVO study = studyService.getStudyByIdx(studyIdx);

        if(study.getUser() != user)
            throw new AuthException("not owned study");

        List<AnnouncementVO> announcements = announcementService.getAnnouncementByStudyIdx(studyIdx);

        List<AnnouncementDTO> announcementDTOS =
                announcements.stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList());

        return new ResponseEntity<>(announcementDTOS, HttpStatus.OK);
    }

    @GetMapping("/announcement/{announcement-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(
            @PathVariable(name = "announcement-idx") Long announcementIdx)
            throws NotFoundException {

        //it can throw not found exception
        AnnouncementVO announcementVO = announcementService.getAnnouncementByIdx(announcementIdx);

        AnnouncementDTO announcementDTO = entityUtil.convertAnnouncementVoToDto(announcementVO);

        return new ResponseEntity<>(announcementDTO,HttpStatus.OK);
    }

    @PostMapping("/study/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO) throws NotFoundException, AuthException {
        //it can throw NotFound, Auth Exception
        AnnouncementVO announcementVO = announcementService.saveAnnouncement(announcementDTO);

        return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(announcementVO));
    }

    @DeleteMapping("/study/announcement/{announcement-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deleteAnnouncement(
            @PathVariable(value = "announcement-idx") Long announcementIdx) throws AuthException, NotFoundException {

        //it can throw NotFoundException
        AnnouncementVO announcement = announcementService.getAnnouncementByIdx(announcementIdx);

        //it can throw AuthException
        UserVO user = userService.getMyUserWithAuthorities();

        if(announcement.getStudy().getUser() != user)
            throw new AuthException("Not owned announcement");

        announcementService.deleteAnnouncement(announcement);

        //response 204 -> no content (delete success)
        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);

    }
}










