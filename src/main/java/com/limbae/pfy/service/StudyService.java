package com.limbae.pfy.service;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.DemandPositionVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.AnnouncementDTO;
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

@Service
public class StudyService {

    StudyRepository studyRepository;
    UserRepository userRepository;
    EntityUtil entityUtil;
    AnnouncementRepository announcementRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, UserRepository userRepository, EntityUtil entityUtil, AnnouncementRepository announcementRepository) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
        this.entityUtil = entityUtil;
        this.announcementRepository = announcementRepository;
    }

    public List<StudyVO> getMyStudyList() {
        Optional<UserVO> userVO;
        if (SecurityUtil.getCurrentUsername().isPresent()) {
            userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());
        } else {
            return null;
        }
        return userVO.map(vo -> studyRepository.findByUserUid(vo.getUid())).orElse(null);
    }

    public StudyVO getStudyByIdx(Long idx) {
        Optional<StudyVO> studyVO = studyRepository.findById(idx);
        return studyVO.orElse(null);
    }

    public StudyVO saveStudy(StudyDTO dto) {
        Optional<UserVO> userVO;
        if (SecurityUtil.getCurrentUsername().isPresent()) {
            userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());
        } else {
            return null;
        }

        StudyVO studyVO = entityUtil.convertStudyDtoToVo(dto);
        userVO.ifPresent(studyVO::setUser);

        return studyRepository.save(studyVO);
    }

    public Optional<List<AnnouncementVO>> getAnnouncementListByStudyIdx(Long studyIdx) {
        Optional<UserVO> userVO;
        if (SecurityUtil.getCurrentUsername().isPresent()) {
            userVO = userRepository.findOneWithAuthoritiesByUsername(SecurityUtil.getCurrentUsername().get());
        } else {
            return Optional.empty();
        }

        Optional<StudyVO> studyVO = studyRepository.findById(studyIdx);
        if (studyVO.isPresent() && userVO.isPresent()) {
            if (studyVO.get().getUser() != userVO.get()) {
                return Optional.empty();
            }

            return Optional.ofNullable(announcementRepository.findByStudyIdx(studyVO.get().getIdx()));
        } else {
            return null;
        }
    }

    public AnnouncementVO saveAnnouncement(AnnouncementDTO announcementDTO) {

        Optional<UserVO> uvo = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        Optional<StudyVO> studyVO = studyRepository.findById(announcementDTO.getStudyIdx());
        if (uvo.isPresent() && studyVO.isPresent()) {
            if (uvo.get() != studyVO.get().getUser())
                return null; //소유권 확인
        } else
            return null;

        AnnouncementVO announcementVO = entityUtil.convertAnnouncementDtoToVo(announcementDTO);
        announcementVO.setStudy(studyRepository.findById(announcementDTO.getStudyIdx()).get());
        announcementRepository.save(announcementVO);

        for (DemandPositionVO vo : announcementVO.getDemandPositionVOSet())
            vo.setStudyAnnouncementIdx(announcementVO.getIdx());
        announcementRepository.save(announcementVO);
        //announcement 저장 뒤 idx를 가져와 announcement idx정보를 demandposition에 저장 후 재 커밋

        return announcementVO;
    }


}
