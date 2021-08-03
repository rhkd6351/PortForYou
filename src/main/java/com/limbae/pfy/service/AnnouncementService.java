package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.repository.*;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import javax.swing.text.html.parser.Entity;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnnouncementService {

    AnnouncementRepository announcementRepository;
    UserRepository userRepository;
    StudyRepository studyRepository;
    EntityUtil entityUtil;
    DemandPositionRepository demandPositionRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository, StudyRepository studyRepository, EntityUtil entityUtil, DemandPositionRepository demandPositionRepository) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.studyRepository = studyRepository;
        this.entityUtil = entityUtil;
        this.demandPositionRepository = demandPositionRepository;
    }

    public List<AnnouncementVO> getAnnouncementByQuery(String query, Pageable pageable){
        return announcementRepository.findByQuery(query,pageable);
    }

    public AnnouncementVO getAnnouncementByIdx(Long idx) throws NotFoundException{
        Optional<AnnouncementVO> announcement = announcementRepository.findById(idx);

        if(announcement.isEmpty())
            throw new NotFoundException("invalid announcement idx");

        return announcement.get();
    }

    public List<AnnouncementVO> getAnnouncementOrderByDesc(){
        return announcementRepository.findTop50ByOrderByIdxDesc();
    }

    public List<AnnouncementVO> getImminentAnnouncement(){
        PageRequest pageRequest = PageRequest.of(0, 50);
        return announcementRepository.findByOrderByEndDateDesc(pageRequest);
    }

    public boolean deleteAnnouncement(AnnouncementVO announcementVO){

        try{
            announcementRepository.delete(announcementVO);
            return true;
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }

    public List<AnnouncementVO> getAnnouncementByStudyIdx(Long studyIdx){
        return announcementRepository.findByStudyIdx(studyIdx);
    }

    public AnnouncementVO saveAnnouncement(AnnouncementDTO announcementDTO) throws NotFoundException, AuthException {

        Optional<UserVO> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        Optional<StudyVO> study = studyRepository.findById(announcementDTO.getStudy().getIdx());

        if(user.isEmpty()) throw new NotFoundException("invalid token");
        if(study.isEmpty()) throw new NotFoundException("invalid study idx");
        if (user.get() != study.get().getUser()) throw new AuthException("Not owned study");


        AnnouncementVO announcement = entityUtil.convertAnnouncementDtoToVo(announcementDTO);
        announcement.setStudy(study.get());
        announcement.setEndDate(LocalDateTime.now().plusDays(7)); //7일 뒤 마감

        for (DemandPositionVO vo : announcement.getDemandPosition()){
            vo.setAnnouncement(announcement);
            demandPositionRepository.save(vo);
        }

        announcementRepository.save(announcement);
        return announcement;
    }




}
