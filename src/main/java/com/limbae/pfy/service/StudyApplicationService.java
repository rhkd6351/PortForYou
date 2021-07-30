package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.StudyApplicationDTO;
import com.limbae.pfy.repository.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
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
    MemberRepository memberRepository;

    @Autowired
    public StudyApplicationService(StudyApplicationRepository studyApplicationRepository, AnnouncementRepository announcementRepository, PortfolioRepository portfolioRepository, PositionRepository positionRepository, MemberRepository memberRepository) {
        this.studyApplicationRepository = studyApplicationRepository;
        this.announcementRepository = announcementRepository;
        this.portfolioRepository = portfolioRepository;
        this.positionRepository = positionRepository;
        this.memberRepository = memberRepository;
    }

    public void acceptApplication(StudyApplicationVO studyApplication) throws AuthException{
        if(studyApplication.getDeclined() != 0)
            throw new AuthException("already accepted or declined"); //TODO exception 유형 바꾸기

        studyApplication.setDeclined(-1L); // -1 : Accepted

        for(DemandPositionVO vo : studyApplication.getAnnouncement().getDemandPosition()) {
            if(vo.getPosition() == studyApplication.getPosition()){
                vo.setApplied(vo.getApplied() + 1); // applied += 1
                break;
            }
        }

        UserVO user = studyApplication.getPortfolio().getUser();
        MemberVO member = MemberVO.builder()
                .user(user)
                .position(studyApplication.getPosition())
                .study(studyApplication.getAnnouncement().getStudy())
                .build();

        memberRepository.save(member);
        studyApplicationRepository.save(studyApplication);

    }

    public StudyApplicationVO getStudyApplicationByIdx(Long idx) throws NotFoundException{
        Optional<StudyApplicationVO> studyApplication = studyApplicationRepository.findById(idx);
        if(studyApplication.isEmpty())
            throw new NotFoundException("invalid studyApplication");

        return studyApplication.get();
    }

    public StudyApplicationVO saveStudyApplication(StudyApplicationVO vo){
        return studyApplicationRepository.save(vo);
    }

    public List<StudyApplicationVO> getStudyApplicationListByStudyIdx(Long studyIdx){
        List<StudyApplicationVO> studyApplications = new ArrayList<>();
        List<AnnouncementVO> announcementVOList = announcementRepository.findByStudyIdx(studyIdx);
        for(AnnouncementVO vo : announcementVOList)
            studyApplications.addAll(studyApplicationRepository.findByAnnouncementIdx(vo.getIdx()));

        return studyApplications;
    }

    public List<StudyApplicationVO> getStudyApplicationListByUid(Long uid){
        List<PortfolioVO> portfolios = portfolioRepository.findByUserUid(uid).get();
        ArrayList<StudyApplicationVO> applicationList = new ArrayList<>();

        for(PortfolioVO portfolioVO : portfolios)
            applicationList.addAll(portfolioVO.getStudyApplications());

        return applicationList;

    }

    public void declineApplication(StudyApplicationVO studyApplication) throws AuthException {
        if(studyApplication.getDeclined() != 0)
            throw new AuthException("already accepted or declined"); //TODO exception 유형 바꾸기

        studyApplication.setDeclined(1L); // -1 : Accepted

        studyApplicationRepository.save(studyApplication);
    }
}
