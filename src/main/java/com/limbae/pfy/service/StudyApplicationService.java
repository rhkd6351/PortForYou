package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.StudyApplicationDTO;
import com.limbae.pfy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudyApplicationService {

    StudyApplicationRepository studyApplicationRepository;
    AnnouncementRepository announcementRepository;
    PortfolioRepository portfolioRepository;
    PositionRepository positionRepository;
    StudyRepository studyRepository;
    UserRepository userRepository;

    @Autowired
    public StudyApplicationService(StudyApplicationRepository studyApplicationRepository,
                                   AnnouncementRepository announcementRepository, PortfolioRepository portfolioRepository,
                                   PositionRepository positionRepository, StudyRepository studyRepository,
                                   UserRepository userRepository) {
        this.studyApplicationRepository = studyApplicationRepository;
        this.announcementRepository = announcementRepository;
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
    }

    public Optional<StudyApplicationVO> getStudyApplicationByIdx(Long idx){
        return studyApplicationRepository.findById(idx);
    }

    public StudyApplicationVO saveStudyApplication(StudyApplicationDTO dto){
        Optional<AnnouncementVO> announcementVO = announcementRepository.findById(dto.getAnnouncement().getIdx());
        Optional<PortfolioVO> portfolioVO = portfolioRepository.findById(dto.getPortfolio().getIdx());
        Optional<PositionVO> positionVO = positionRepository.findById(dto.getPosition().getIdx());
        if(announcementVO.isEmpty() || portfolioVO.isEmpty() || positionVO.isEmpty())
            return null;

        StudyApplicationVO build = StudyApplicationVO.builder()
                .announcement(announcementVO.get())
                .portfolio(portfolioVO.get())
                .position(positionVO.get())
                .declined(0L)
                .build();

        return studyApplicationRepository.saveAndFlush(build);
    }

    public StudyApplicationVO saveStudyApplication(StudyApplicationVO vo){
        return studyApplicationRepository.saveAndFlush(vo);
    }

    public List<StudyApplicationVO> getStudyApplicationListByStudyIdx(Long studyIdx){
        List<StudyApplicationVO> list = new ArrayList<>();
        List<AnnouncementVO> announcementVOList = announcementRepository.findByStudyIdx(studyIdx);
        for(AnnouncementVO vo : announcementVOList) {
            list.addAll(studyApplicationRepository.findByAnnouncementIdx(vo.getIdx()));
        }

        return list;
    }

    public List<StudyApplicationVO> getStudyApplicationLIstByUid(Long uid){
        UserVO userVO = userRepository.findOneWithPortfolioByUid(uid).get();
        ArrayList<StudyApplicationVO> applicationList = new ArrayList<>();

        for(PortfolioVO portfolioVO : userVO.getPortfolio())
            applicationList.addAll(portfolioVO.getStudyApplications());

        return applicationList;

    }

}
