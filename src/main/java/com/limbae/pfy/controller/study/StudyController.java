package com.limbae.pfy.controller.study;


import com.limbae.pfy.domain.study.MemberVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.service.study.AnnouncementService;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
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
public class StudyController {

    UserServiceInterfaceImpl userService;
    EntityUtil entityUtil;
    StudyServiceInterfaceImpl studyService;
    AnnouncementService announcementService;

    public StudyController(UserServiceInterfaceImpl userService, EntityUtil entityUtil, StudyServiceInterfaceImpl studyService, AnnouncementService announcementService) {
        this.userService = userService;
        this.entityUtil = entityUtil;
        this.studyService = studyService;
        this.announcementService = announcementService;
    }

    @GetMapping("/user/studies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<StudyDTO>> getMyStudyList(
            @RequestParam(required = false) boolean applied) throws Exception {

        //it can throw auth exception
        UserVO user = userService.getByAuth();

        // applied = true -> 가입 스터디
        if(applied){
            return ResponseEntity.ok(studyService.getAppliedStudiesByUid(user.getUid()).stream().map(
                    i -> {
                        StudyDTO studyDTO = entityUtil.convertStudyVoToDto(i);
                        studyDTO.setNumberOfMembers(i.getMembers().size() + 1);
                        return studyDTO;
                    }
            ).collect(Collectors.toList()));
        }

        //관리 스터디
        return ResponseEntity.ok(studyService.getByUid(user.getUid()).stream().map(
                        i -> {
                            StudyDTO studyDTO = entityUtil.convertStudyVoToDto(i);
                            studyDTO.setNumberOfMembers(i.getMembers().size() + 1);
                            return studyDTO;
                        }
                ).collect(Collectors.toList()));
    }

    @GetMapping("/study/{study-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyDTO> getStudyByIdx(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        //it can throw NotFoundException
        StudyVO study = studyService.getByIdx(studyIdx);

        //it can throw AuthException
        UserVO user = userService.getByAuth();

        if(study.getUser() != user)
            throw new AuthException("not owned Study");

        return ResponseEntity.ok(entityUtil.convertStudyVoToDto(study));
    }

    @PostMapping("/study")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<StudyDTO> saveStudy(
            @RequestBody StudyDTO studyDTO) throws NotFoundException, AuthException {

        //it can throw NotFound, Auth Exception
        StudyVO study = studyService.update(studyDTO);

        return ResponseEntity.ok(entityUtil.convertStudyVoToDto(study));
    }

    @DeleteMapping("/study/{study-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deleteStudy(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        //it can throw NotFoundException
        StudyVO study = studyService.getByIdx(studyIdx);

        //it can throw AuthException
        UserVO user = userService.getByAuth();

        if(study.getUser() != user)
            throw new AuthException("not owned Study");

        studyService.delete(study);

        //response 204 -> no content (delete success)
        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);

    }

    @GetMapping("/study/{study-idx}/members")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<MemberDTO>> getMembersByStudyIdx(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        //it can throw Auth, NotFound Exception
        StudyVO study = studyService.getByIdx(studyIdx);

        //it can throw Auth, NotFound Exception
        UserVO loginUser = userService.getByAuth();

        UserVO manager = study.getUser();
        List<MemberVO> members = study.getMembers();

        if(!(loginUser == manager || members.stream().map(MemberVO::getUser).collect(Collectors.toSet()).contains(loginUser)))
            throw new AuthException("not belong to study");

        List<MemberDTO> membersWithManager = new ArrayList<>();
        membersWithManager.add(MemberDTO.builder().user(entityUtil.convertUserVoToDto(manager)).build());
        membersWithManager.addAll(members.stream().map(i ->
                MemberDTO.builder()
                        .user(entityUtil.convertUserVoToDto(i.getUser()))
                        .position(entityUtil.convertPositionVoToDto(i.getPosition()))
                        .build()
        ).collect(Collectors.toList()));

        return new ResponseEntity<>(membersWithManager, HttpStatus.OK);

    }

}










