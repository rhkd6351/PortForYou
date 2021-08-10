package com.limbae.pfy.controller.study;


import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.AnnouncementQueryResultDTO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.service.study.AnnouncementService;
import com.limbae.pfy.service.study.DemandPositionService;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping(value = {"/api"})
@Slf4j
public class AnnouncementController {

    UserServiceInterfaceImpl userService;
    EntityUtil entityUtil;
    StudyServiceInterfaceImpl studyService;
    AnnouncementService announcementService;
    PortfolioServiceInterfaceImpl portfolioService;
    DemandPositionService demandPositionService;

    public AnnouncementController(UserServiceInterfaceImpl userService, EntityUtil entityUtil, StudyServiceInterfaceImpl studyService, AnnouncementService announcementService, PortfolioServiceInterfaceImpl portfolioService, DemandPositionService demandPositionService) {
        this.userService = userService;
        this.entityUtil = entityUtil;
        this.studyService = studyService;
        this.announcementService = announcementService;
        this.portfolioService = portfolioService;
        this.demandPositionService = demandPositionService;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 10) //10분마다 announcement 마감처리
    public void closeAnnouncementScheduler(){
        List<AnnouncementVO> announcements = announcementService.getAfterEndDate();
        for (AnnouncementVO announcement : announcements) {
            announcement.setActivated(false);
            announcementService.update(announcement);
        }
        log.info("closeAnnouncementScheduler is called");
    }

    @GetMapping("/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementQueryResultDTO> getAnnouncementList(
            @RequestParam(name = "kind", required = true) String kind,
            @RequestParam(name = "pno", required = true) Integer pno,
            @RequestParam(name = "query", required = false) String query) throws Exception {

        AnnouncementQueryResultDTO dto = new AnnouncementQueryResultDTO();
        if(pno < 1) pno = 1; if(pno > 999) pno = 999;
        dto.setPno(pno);

        PageRequest pageRequest = PageRequest.of(pno - 1, 36);

        if(kind.equals("new")){
            Page<AnnouncementVO> announcements = announcementService.getOrderByDesc(pageRequest);
            dto.setLastPno(Math.max(announcements.getTotalPages(), 1));
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
            dto.setLastPno(announcements.getTotalPages());
        }

        else if(kind.equals("imminent")){
            Page<AnnouncementVO> announcements = announcementService.getImminent(pageRequest);
            dto.setLastPno(Math.max(announcements.getTotalPages(), 1));
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
            dto.setLastPno(announcements.getTotalPages());
        }

        else if(kind.equals("search")){
            if(query == null) throw new MissingRequestValueException("param query is null");
            Page<AnnouncementVO> announcements = announcementService.getByQuery(query, pageRequest);
            dto.setLastPno(Math.max(announcements.getTotalPages(), 1));
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
        }

        // TODO 아직 포지션만 선택해서 추천하는 기능밖에 구현 안됨. 이후 announcement에 tech정보도 넣어서 추천하는 기능 구현할것
        else if(kind.equals("recommend")){ // TODO 로직 서비스단으로 분리하는게 적합한가? 응 적합해
            List<AnnouncementVO> announcements = announcementService.getRecommend();
            dto.setLastPno(Math.max((int) Math.ceil(announcements.size() / 36.), 1)); // TODO 자체적으로 page 구현하였음. repository에서 받아오는방법으로 수정할것

            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(
                    announcements.subList(36 * (pno-1), Math.min(announcements.size(), 36 * pno))
                            .stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
        }
        else
            throw new MissingRequestValueException("param kind is null or invalid");

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/study/{study-idx}/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementList(
            @PathVariable(name = "study-idx") Long studyIdx) throws AuthException, NotFoundException {

        UserVO user = userService.getByAuth();
        StudyVO study = studyService.getByIdx(studyIdx);

        if(study.getUser() != user)
            throw new AuthException("not owned study");

        List<AnnouncementVO> announcements = announcementService.getByStudyIdx(studyIdx);

        List<AnnouncementDTO> announcementDTOS =
                announcements.stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList());

        return new ResponseEntity<>(announcementDTOS, HttpStatus.OK);
    }

    @GetMapping("/announcement/{announcement-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(
            @PathVariable(name = "announcement-idx") Long announcementIdx)
            throws NotFoundException {

        AnnouncementVO announcementVO = announcementService.getByIdx(announcementIdx);
        AnnouncementDTO announcementDTO = entityUtil.convertAnnouncementVoToDto(announcementVO);

        return new ResponseEntity<>(announcementDTO,HttpStatus.OK);
    }

    @PostMapping("/study/{study-idx}/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO,
            @PathVariable(value = "study-idx") Long studyIdx) throws NotFoundException, AuthException {

        announcementDTO.setStudy(StudyDTO.builder().idx(studyIdx).build());
        AnnouncementVO announcementVO = announcementService.update(announcementDTO);

        return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(announcementVO));
    }

    @DeleteMapping("/announcement/{announcement-idx}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> deleteAnnouncement(
            @PathVariable(value = "announcement-idx") Long announcementIdx) throws AuthException, NotFoundException {

        AnnouncementVO announcement = announcementService.getByIdx(announcementIdx);
        UserVO user = userService.getByAuth();

        if(announcement.getStudy().getUser() != user)
            throw new AuthException("Not owned announcement");

        announcementService.delete(announcement);

        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);

    }
}










