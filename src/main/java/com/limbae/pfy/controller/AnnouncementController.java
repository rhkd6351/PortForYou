package com.limbae.pfy.controller;


import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.AnnouncementQueryResultDTO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping(value = {"/api"})
@Slf4j
public class AnnouncementController {

    UserService userService;
    EntityUtil entityUtil;
    StudyService studyService;
    AnnouncementService announcementService;
    PortfolioService portfolioService;
    DemandPositionService demandPositionService;

    public AnnouncementController(UserService userService, EntityUtil entityUtil, StudyService studyService, AnnouncementService announcementService, PortfolioService portfolioService, DemandPositionService demandPositionService) {
        this.userService = userService;
        this.entityUtil = entityUtil;
        this.studyService = studyService;
        this.announcementService = announcementService;
        this.portfolioService = portfolioService;
        this.demandPositionService = demandPositionService;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 10) //10분마다 announcement 마감처리
    public void closeAnnouncementScheduler(){
        List<AnnouncementVO> announcements = announcementService.getAnnouncementAfterEndDate();
        for (AnnouncementVO announcement : announcements) {
            announcement.setActivated(false);
            announcementService.saveAnnouncement(announcement);
        }
        log.info("closeAnnouncementScheduler is called");
    }

    @GetMapping("/announcements")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementQueryResultDTO> getAnnouncementList(
            @RequestParam(name = "kind", required = true) String kind,
            @RequestParam(name = "pno", required = true) Integer pno,
            @RequestParam(name = "query", required = false) String query) throws MissingRequestValueException, AuthException, DataFormatException {

        AnnouncementQueryResultDTO dto = new AnnouncementQueryResultDTO();
        if(pno < 1) pno = 1; if(pno > 999) pno = 999;
        dto.setPno(pno);

        PageRequest pageRequest = PageRequest.of(pno - 1, 36);

        if(kind.equals("new")){
            Page<AnnouncementVO> announcements = announcementService.getAnnouncementOrderByDesc(pageRequest);
            dto.setLastPno(announcements.getTotalPages());
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
            dto.setLastPno(announcements.getTotalPages());
        }

        else if(kind.equals("imminent")){
            Page<AnnouncementVO> announcements = announcementService.getImminentAnnouncement(pageRequest);
            dto.setLastPno(announcements.getTotalPages());
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
            dto.setLastPno(announcements.getTotalPages());
        }

        else if(kind.equals("search")){
            if(query == null) throw new MissingRequestValueException("param query is null");
            Page<AnnouncementVO> announcements = announcementService.getAnnouncementByQuery(query, pageRequest);
            dto.setLastPno(announcements.getTotalPages());
            if(pno > dto.getLastPno()) throw new DataFormatException("pno is too big");
            dto.setAnnouncements(announcements.getContent().stream().map(entityUtil::convertAnnouncementVoToDto).collect(Collectors.toList()));
        }

        // TODO 아직 포지션만 선택해서 추천하는 기능밖에 구현 안됨. 이후 announcement에 tech정보도 넣어서 추천하는 기능 구현할것
        else if(kind.equals("recommend")){ // TODO 로직 서비스단으로 분리하는게 적합한가?
            //it can throw auth exception
            UserVO user = userService.getMyUserWithAuthorities();
            List<PortfolioVO> portfolios = portfolioService.getPortfoliosByUid(user.getUid());
            List<AnnouncementVO> announcements = new ArrayList<>();
            Set<PositionVO> positions = new HashSet<>();

            for (PortfolioVO portfolio : portfolios)
                positions.add(portfolio.getPosition());

            for (PositionVO position : positions)
                announcements.addAll(announcementService.getAnnouncementByPosition(position));

            announcements = announcements.stream().distinct().collect(Collectors.toList()); //중복 제거

            dto.setLastPno((int) Math.ceil(announcements.size() / 36.)); // TODO 자체적으로 page 구현하였음. repository에서 받아오는방법으로 수정할것
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

    @PostMapping("/study/{study-idx}/announcement")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AnnouncementDTO> saveAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO,
            @PathVariable(value = "study-idx") Long studyIdx) throws NotFoundException, AuthException {

        announcementDTO.setStudy(StudyDTO.builder().idx(studyIdx).build());
        //it can throw NotFound, Auth Exception
        AnnouncementVO announcementVO = announcementService.saveAnnouncement(announcementDTO);

        return ResponseEntity.ok(entityUtil.convertAnnouncementVoToDto(announcementVO));
    }

    @DeleteMapping("/announcement/{announcement-idx}")
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










