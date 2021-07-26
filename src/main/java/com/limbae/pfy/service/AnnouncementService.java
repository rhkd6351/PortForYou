package com.limbae.pfy.service;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.StudyApplicationVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.repository.AnnouncementRepository;
import com.limbae.pfy.repository.StudyApplicationRepository;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnnouncementService {

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    StudyApplicationRepository studyApplicationRepository;

    @Autowired
    EntityUtil entityUtil;

    public AnnouncementVO getAnnouncementByIdx(Long idx){
        return announcementRepository.findById(idx).orElse(null);
    }

    public List<AnnouncementVO> getAnnouncementOrderByDesc(){
        return announcementRepository.findTop50ByOrderByIdxDesc();
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

    public AnnouncementVO saveAnnouncement(AnnouncementVO vo){
        return announcementRepository.save(vo);
    }




}
