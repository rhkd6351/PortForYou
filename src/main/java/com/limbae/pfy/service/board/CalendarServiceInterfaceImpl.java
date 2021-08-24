package com.limbae.pfy.service.board;


import com.limbae.pfy.domain.board.CalendarVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.board.CalendarDTO;
import com.limbae.pfy.repository.board.CalendarRepository;
import com.limbae.pfy.service.study.StudyServiceInterface;
import com.limbae.pfy.service.user.UserServiceInterface;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CalendarServiceInterfaceImpl implements CalendarServiceInterface{

    CalendarRepository calendarRepository;

    StudyServiceInterface studyService;
    UserServiceInterface userService;

    @Autowired
    public CalendarServiceInterfaceImpl(CalendarRepository calendarRepository, StudyServiceInterface studyService, UserServiceInterface userService) {
        this.calendarRepository = calendarRepository;
        this.studyService = studyService;
        this.userService = userService;
    }

    @Override
    public CalendarVO update(CalendarDTO dto) throws AuthException, NotFoundException {

        UserVO user = userService.getByAuth();
        StudyVO study = studyService.getByIdx(dto.getStudy().getIdx());

        CalendarVO calendar = null;
        if(dto.getIdx() != null){
            calendar = this.getByIdx(dto.getIdx());

            if(calendar.getUser() != user)
                throw new AuthException("not owned calendar");

            calendar.setTitle(dto.getTitle());
            calendar.setContent(dto.getContent());
            calendar.setFromDate(dto.getFromDate());
            calendar.setToDate(dto.getToDate());

        }else{
            calendar = CalendarVO.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .user(user)
                    .study(study)
                    .fromDate(dto.getFromDate())
                    .toDate(dto.getToDate())
                    .build();
        }

        return calendarRepository.save(calendar);
    }

    @Override
    public CalendarVO update(CalendarVO vo) {
        return calendarRepository.save(vo);
    }

    @Override
    public List<CalendarVO> getByStudyIdx(Long studyIdx) throws NotFoundException, AuthException {
        UserVO user = userService.getByAuth();
        StudyVO study = studyService.getByIdx(studyIdx);

        studyService.memberCheck(user.getUid());

        return study.getCalendars();
    }

    @Override
    public CalendarVO getByIdx(Long idx) throws NotFoundException {
        Optional<CalendarVO> calendar = calendarRepository.findById(idx);
        if(calendar.isEmpty())
            throw new NotFoundException("invalid calendar idx");

        return calendar.get();
    }

    @Override
    public void delete(long idx) throws Exception {
        CalendarVO calendar = this.getByIdx(idx);
        try{
            calendarRepository.delete(calendar);
        }catch (Exception e){
            log.warn(e.getMessage());
            throw e;
        }
    }
}
