package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.repository.*;
import com.limbae.pfy.service.user.PortfolioServiceInterface;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterface;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnnouncementService implements AnnouncementServiceInterface {

    AnnouncementRepository announcementRepository;

    StudyServiceInterface studyService;
    DemandPositionService demandPositionService;
    UserServiceInterface userService;
    PortfolioServiceInterface portfolioService;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, StudyServiceInterface studyService, DemandPositionService demandPositionService) {
        this.announcementRepository = announcementRepository;
        this.studyService = studyService;
        this.demandPositionService = demandPositionService;
    }

    public AnnouncementVO getByIdx(Long idx) throws NotFoundException{
        Optional<AnnouncementVO> announcement = announcementRepository.findById(idx);

        if(announcement.isEmpty())
            throw new NotFoundException("invalid announcement idx");

        return announcement.get();
    }

    public List<AnnouncementVO> getByStudyIdx(Long studyIdx) throws NotFoundException {
        StudyVO study = studyService.getByIdx(studyIdx);
        return announcementRepository.findByStudyIdx(study.getIdx());
    }

    public AnnouncementVO update(AnnouncementDTO dto) throws NotFoundException, AuthException {

        StudyVO study = studyService.getByIdx(dto.getStudy().getIdx());
        studyService.managerCheck(study.getIdx());

        List<DemandPositionVO> demandPosition = dto.getDemandPosition().stream().map(
                i -> {
                    try {
                        return demandPositionService.update(i);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());


        AnnouncementVO announcement = null;
        if(dto.getIdx() != null){
            announcement = this.getByIdx(dto.getIdx());
            announcement.setTitle(dto.getTitle());
            announcement.setContent(dto.getContent());
            announcement.setActivated(true);
            announcement.setDemandPosition(demandPosition);
            announcement.setStudy(study);

        }else{
            announcement = AnnouncementVO.builder()
                    .content(dto.getContent())
                    .title(dto.getTitle())
                    .demandPosition(demandPosition)
                    .activated(true)
                    .study(study)
                    .endDate(LocalDateTime.now().plusDays(7))
                    .build();
        }

        announcementRepository.save(announcement);
        return announcement;
    }

    public AnnouncementVO update(AnnouncementVO announcementVO){
        return announcementRepository.save(announcementVO);
    }

    public void delete(AnnouncementVO announcementVO){
        try{
            announcementRepository.delete(announcementVO);
        }catch (Exception e){
            log.warn(e.getMessage());
            throw e;
        }
    }


    public Page<AnnouncementVO> getByQuery(String query, Pageable pageable){
        return announcementRepository.findByQuery(query,pageable);
    }

    public Page<AnnouncementVO> getOrderByDesc(Pageable pageable){
        return announcementRepository.findByOrderByIdxDesc(pageable);
    }

    public Page<AnnouncementVO> getImminent(Pageable pageable){
        return announcementRepository.findByOrderByEndDateDesc(pageable);
    }

    public List<AnnouncementVO> getRecommend() throws Exception {
        UserVO user = userService.getByAuth();
        List<PortfolioVO> portfolios = portfolioService.getByUid(user.getUid());
        Set<PositionVO> positions = new HashSet<>();
        List<AnnouncementVO> announcements = new ArrayList<>();

        for (PortfolioVO portfolio : portfolios)
            positions.add(portfolio.getPosition());

        for (PositionVO position : positions)
            announcements.addAll(this.getByPosition(position));

        announcements = announcements.stream().distinct().collect(Collectors.toList()); //중복 제거
        return announcements;
    }

    public List<AnnouncementVO> getAfterEndDate(){
        return announcementRepository.findByAfterEndDate();
    }

    public List<AnnouncementVO> getByPosition(PositionVO positionVO){
        return announcementRepository.findByPosition(positionVO);
    }




}
