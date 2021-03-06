package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.study.MemberVO;
import com.limbae.pfy.domain.study.StudyCategoryVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.study.StudyDTO;
import com.limbae.pfy.repository.study.StudyRepository;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.etc.StudyCategoryService;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudyServiceInterfaceImpl implements StudyServiceInterface {

    StudyRepository studyRepository;

    UserServiceInterfaceImpl userService;
    StudyCategoryService studyCategoryService;
    MemberService memberService;
    PositionService positionService;

    public StudyServiceInterfaceImpl(StudyRepository studyRepository, UserServiceInterfaceImpl userService, StudyCategoryService studyCategoryService, MemberService memberService, PositionService positionService) {
        this.studyRepository = studyRepository;
        this.userService = userService;
        this.studyCategoryService = studyCategoryService;
        this.memberService = memberService;
        this.positionService = positionService;
    }

    public List<StudyVO> getByUid(Long uid) throws Exception {

        UserVO user = userService.getByUid(uid);
        return studyRepository.findByUserUid(user.getUid());
    }

    public List<StudyVO> getAppliedStudiesByUid(Long uid) throws Exception {
        UserVO user = userService.getByUid(uid);
        return user.getMembers().stream().map(MemberVO::getStudy).collect(Collectors.toList());
    }

    public StudyVO getByIdx(Long idx) throws NotFoundException {
        Optional<StudyVO> study = studyRepository.findById(idx);

        if(study.isEmpty())
            throw new NotFoundException("invalid study idx");

        return study.get();
    }

    public StudyVO update(StudyDTO dto) throws NotFoundException, AuthException {

        UserVO user = userService.getByAuth();

        StudyCategoryVO studyCategory = studyCategoryService.getByIdx(dto.getStudyCategory().getIdx());

        StudyVO study = null;
        if(dto.getIdx() != null){
            study = this.getByIdx(dto.getIdx());
            study.setTitle(dto.getTitle());
            study.setContent(dto.getContent());
            study.setStudyCategory(studyCategory);
            study.setUser(user);
        }else{
            study = StudyVO.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .studyCategory(studyCategory)
                    .user(user)
                    .build();
        }

        StudyVO studyVO = studyRepository.save(study);

        MemberVO manager = MemberVO.builder()
                .study(studyVO)
                .user(user)
                .position(positionService.getByIdx(8L)) //Manager position
                .build();

        memberService.save(manager);

        return studyVO;
    }

    public void delete(StudyVO vo){
        try{
            studyRepository.delete(vo);
        }catch (Exception e){
            log.warn(e.getMessage());
            throw e;
        }
    }

    public void memberCheck(Long idx) throws AuthException, NotFoundException {
        StudyVO study = this.getByIdx(idx);
        UserVO user = userService.getByAuth();

        UserVO manager = study.getUser();
        List<MemberVO> members = study.getMembers();

        if(!(user == manager || members.stream().map(MemberVO::getUser).collect(Collectors.toSet()).contains(user)))
            throw new AuthException("not belong to study");
    }

    public void managerCheck(Long idx) throws NotFoundException, AuthException {
        StudyVO study = this.getByIdx(idx);
        UserVO user = userService.getByAuth();

        UserVO manager = study.getUser();

        if(!(user == manager))
            throw new AuthException("not owned study");
    }


}
