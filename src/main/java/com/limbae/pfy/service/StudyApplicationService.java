package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.StudyApplicationDTO;
import com.limbae.pfy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyApplicationService {

    StudyApplicationRepository studyApplicationRepository;
    AnnouncementRepository announcementRepository;
    PortfolioRepository portfolioRepository;
    PositionRepository positionRepository;
    StudyRepository studyRepository;

    @Autowired
    public StudyApplicationService(StudyApplicationRepository studyApplicationRepository, AnnouncementRepository announcementRepository, PortfolioRepository portfolioRepository, PositionRepository positionRepository, StudyRepository studyRepository) {
        this.studyApplicationRepository = studyApplicationRepository;
        this.announcementRepository = announcementRepository;
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.studyRepository = studyRepository;
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
                .build();

        return studyApplicationRepository.saveAndFlush(build);
    }

    public List<StudyApplicationVO> getStudyApplicationListByStudyIdx(Long studyIdx){
        Optional<AnnouncementVO> announcementVO = announcementRepository.findByStudyIdx(studyIdx).stream().findAny();
        if(announcementVO.isEmpty()) return null;

        return studyApplicationRepository.findByAnnouncementIdx(announcementVO.get().getIdx());
    }

}
