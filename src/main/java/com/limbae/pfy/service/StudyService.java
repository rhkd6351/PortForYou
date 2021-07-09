package com.limbae.pfy.service;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.DemandPositionVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.DemandPositionDTO;
import com.limbae.pfy.dto.StudyDTO;
import com.limbae.pfy.repository.AnnouncementRepository;
import com.limbae.pfy.repository.StudyRepository;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudyService {

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    AnnouncementRepository announcementRepository;


    public List<StudyVO> getMyStudyList(){
        Optional<UserVO> userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());
        List<StudyVO> studyList = studyRepository.findByUserUid(userVO.get().getUid());

        return studyList;
    }

    public StudyVO saveStudy(StudyDTO dto){
        Optional<UserVO> userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());

        StudyVO studyVO = entityUtil.convertStudyDtoToVo(dto);
        studyVO.setUser(userVO.get());

        return studyRepository.save(studyVO);
    }

    public Optional<List<AnnouncementVO>> getAnnouncementListByStudyIdx(Long studyIdx){
        Optional<UserVO> userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());
        Optional<StudyVO> studyVO = studyRepository.findById(studyIdx);
        if(studyVO.get().getUser() != userVO.get()) {
            return Optional.empty();
        }

        return Optional.ofNullable(announcementRepository.findByStudyIdx(studyVO.get().getIdx()));
    }

    public AnnouncementVO saveAnnouncement(AnnouncementDTO announcementDTO){

        AnnouncementVO announcementVO = entityUtil.convertAnnouncementDtoToVo(announcementDTO);
        announcementVO.setStudy(studyRepository.findById(announcementDTO.getStudyIdx()).get());
        announcementRepository.save(announcementVO);

        for(DemandPositionVO vo : announcementVO.getDemandPositionVOSet())
            vo.setStudyAnnouncementIdx(announcementVO.getIdx());
        announcementRepository.save(announcementVO);
        //announcement 저장 뒤 idx를 가져와 announcement idx정보를 demandposition에 저장 후 커밋

        return announcementVO;
    }

}
