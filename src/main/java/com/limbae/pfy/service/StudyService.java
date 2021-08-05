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
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudyService {

    StudyRepository studyRepository;
    UserRepository userRepository;
    EntityUtil entityUtil;
    AnnouncementRepository announcementRepository;
    StudyCategoryRepository studyCategoryRepository;
    DemandPositionRepository demandPositionRepository;
    UserService userService;

    @Autowired
    public StudyService(StudyRepository studyRepository, UserRepository userRepository, EntityUtil entityUtil, AnnouncementRepository announcementRepository, StudyCategoryRepository studyCategoryRepository, DemandPositionRepository demandPositionRepository, UserService userService) {
        this.studyRepository = studyRepository;
        this.userRepository = userRepository;
        this.entityUtil = entityUtil;
        this.announcementRepository = announcementRepository;
        this.studyCategoryRepository = studyCategoryRepository;
        this.demandPositionRepository = demandPositionRepository;
        this.userService = userService;
    }


    public List<StudyVO> getStudiesByUid(Long uid) {
        // 여기서 uid는 이미 검증된 번호이므로 (영속성에서 가져온 유저정보의 uid가 파라미터로 넘어옴)
        // uid 검증과 예외처리는 생략한다.
        return studyRepository.findByUserUid(uid);
    }

    public List<StudyVO> getAppliedStudiesByUid(Long uid) {
        UserVO user = userRepository.getById(uid);
        List<StudyVO> studies = user.getMembers().stream().map(MemberVO::getStudy).collect(Collectors.toList());
        return studies;
    }

    public StudyVO getStudyByIdx(Long idx) throws NotFoundException {
        Optional<StudyVO> study = studyRepository.findById(idx);
        if(study.isEmpty())
            throw new NotFoundException("invalid study idx");

        return study.get();
    }

    public StudyVO saveStudy(StudyDTO dto) throws NotFoundException, AuthException {

        Optional<UserVO> user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
        if(user.isEmpty())
            throw new AuthException("invalid token");

        Optional<StudyCategoryVO> studyCategory = studyCategoryRepository.findById(dto.getStudyCategory().getIdx());
        if(studyCategory.isEmpty())
            throw new NotFoundException("invalid studyCategoryIdx");

        StudyVO study = entityUtil.convertStudyDtoToVo(dto);
        study.setStudyCategory(studyCategory.get());
        study.setUser(user.get());

        return studyRepository.save(study);
    }

    public boolean deleteStudy(StudyVO vo){
        try{
            studyRepository.delete(vo);
            return true;
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }

    public void memberCheck(Long idx) throws AuthException, NotFoundException {
        StudyVO study = this.getStudyByIdx(idx);
        UserVO user = userService.getMyUserWithAuthorities();

        UserVO manager = study.getUser();
        List<MemberVO> members = study.getMembers();

        if(!(user == manager || members.stream().map(MemberVO::getUser).collect(Collectors.toSet()).contains(user)))
            throw new AuthException("not belong to study");
    }

    public void managerCheck(Long idx) throws NotFoundException, AuthException {
        StudyVO study = this.getStudyByIdx(idx);
        UserVO user = userService.getMyUserWithAuthorities();

        UserVO manager = study.getUser();

        if(!(user == manager))
            throw new AuthException("not owned study");
    }


}
