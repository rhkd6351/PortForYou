package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.domain.study.MemberVO;
import com.limbae.pfy.domain.study.StudyApplicationVO;
import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.StudyApplicationDTO;
import com.limbae.pfy.repository.*;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.user.PortfolioServiceInterface;
import com.limbae.pfy.service.user.UserServiceInterface;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudyApplicationService {

    StudyApplicationRepository studyApplicationRepository;

    MemberService memberService;
    AnnouncementServiceInterface announcementService;
    PortfolioServiceInterface portfolioService;
    UserServiceInterface userService;
    PositionService positionService;

    public StudyApplicationService(StudyApplicationRepository studyApplicationRepository, MemberService memberService, AnnouncementServiceInterface announcementService, PortfolioServiceInterface portfolioService, UserServiceInterface userService, PositionService positionService) {
        this.studyApplicationRepository = studyApplicationRepository;
        this.memberService = memberService;
        this.announcementService = announcementService;
        this.portfolioService = portfolioService;
        this.userService = userService;
        this.positionService = positionService;
    }

    public StudyApplicationVO getByIdx(Long idx) throws NotFoundException{
        Optional<StudyApplicationVO> studyApplication = studyApplicationRepository.findById(idx);

        if(studyApplication.isEmpty())
            throw new NotFoundException("invalid studyApplication");

        return studyApplication.get();
    }

    public StudyApplicationVO save(StudyApplicationDTO dto) throws Exception {

        UserVO user = userService.getByAuth();
        AnnouncementVO announcement = announcementService.getByIdx(dto.getAnnouncement().getIdx());
        PositionVO position = positionService.getByIdx(dto.getPosition().getIdx());
        PortfolioVO portfolio = portfolioService.getByIdx(dto.getPortfolio().getIdx());

        if(user != portfolio.getUser()) throw new AuthException("not owned portfolio");

        if(!announcement.isActivated()) throw new NotActiveException("announcement is closed");

        StudyApplicationVO studyApplication = StudyApplicationVO.builder()
                .announcement(announcement)
                .portfolio(portfolio)
                .position(position)
                .declined(0L)
                .build();

        return studyApplicationRepository.save(studyApplication);
    }

    public StudyApplicationVO save(StudyApplicationVO vo) throws Exception {
        return studyApplicationRepository.save(vo);
    }

    public List<StudyApplicationVO> getByStudyIdx(Long studyIdx) throws NotFoundException {
        List<StudyApplicationVO> studyApplications = new ArrayList<>();
        List<AnnouncementVO> announcements = announcementService.getByStudyIdx(studyIdx);
        for(AnnouncementVO vo : announcements)
            studyApplications.addAll(studyApplicationRepository.findByAnnouncementIdx(vo.getIdx()));

        return studyApplications;
    }

    public List<StudyApplicationVO> getByUid(Long uid) throws Exception {
        UserVO user = userService.getByUid(uid);
        List<PortfolioVO> portfolios = portfolioService.getByUid(user.getUid());

        ArrayList<StudyApplicationVO> applicationList = new ArrayList<>();
        for(PortfolioVO portfolioVO : portfolios)
            applicationList.addAll(portfolioVO.getStudyApplications());

        return applicationList;
    }

    public void decline(StudyApplicationVO studyApplication) throws Exception {

        if(studyApplication.getDeclined() != 0)
            throw new AuthException("already accepted or declined"); //TODO exception 유형 바꾸기

        studyApplication.setDeclined(1L); // -1 : Accepted

        this.save(studyApplication);
    }

    public void accept(StudyApplicationVO studyApplication) throws Exception {

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

        memberService.save(member);
        this.save(studyApplication);
    }
}
