package com.limbae.pfy.controller.board;


import com.limbae.pfy.domain.board.CalendarVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.board.CalendarDTO;
import com.limbae.pfy.dto.etc.ResponseObjectDTO;
import com.limbae.pfy.dto.study.StudyDTO;
import com.limbae.pfy.service.board.CalendarServiceInterface;
import com.limbae.pfy.service.study.StudyServiceInterface;
import com.limbae.pfy.service.user.UserServiceInterface;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class CalendarController {

    CalendarServiceInterface calendarService;
    StudyServiceInterface studyService;
    UserServiceInterface userService;
    EntityUtil entityUtil;

    @Autowired
    public CalendarController(CalendarServiceInterface calendarService, StudyServiceInterface studyService, UserServiceInterface userService, EntityUtil entityUtil) {
        this.calendarService = calendarService;
        this.studyService = studyService;
        this.userService = userService;
        this.entityUtil = entityUtil;
    }

    @GetMapping("/study/{study-idx}/calendars")
    public ResponseEntity<List<CalendarDTO>> getCalendarsByStudyIdx(
            @PathVariable(value = "study-idx") Long studyIdx) throws NotFoundException, AuthException {
        StudyVO study = studyService.getByIdx(studyIdx);
        studyService.memberCheck(studyIdx);

        List<CalendarVO> calendars = calendarService.getByStudyIdx(study.getIdx());
        List<CalendarDTO> calendarDTOs = calendars.stream().map(entityUtil::convertCalendarVoToDto).collect(Collectors.toList());
        return ResponseEntity.ok(calendarDTOs);
    }

    @GetMapping("/study/calendar/{calendar-idx}")
    public ResponseEntity<CalendarDTO> getCalendarsByIdx(
            @PathVariable(value = "calendar-idx") Long calendarIdx) throws NotFoundException, AuthException {

        CalendarVO calendar = calendarService.getByIdx(calendarIdx);
        studyService.memberCheck(calendar.getStudy().getIdx());

        return ResponseEntity.ok(entityUtil.convertCalendarVoToDto(calendar));
    }

    @PostMapping("/study/{study-idx}/calendar")
    public ResponseEntity<CalendarDTO> updateCalendar(
            @PathVariable(value = "study-idx") Long studyIdx,
            @RequestBody CalendarDTO calendarDTO) throws NotFoundException, AuthException {
        StudyVO study = studyService.getByIdx(studyIdx);
        studyService.memberCheck(study.getIdx());
        calendarDTO.setStudy(StudyDTO.builder().idx(study.getIdx()).build());

        CalendarVO calendar = calendarService.update(calendarDTO);

        return new ResponseEntity<>(entityUtil.convertCalendarVoToDto(calendar),HttpStatus.CREATED);
    }

    @DeleteMapping("/study/calendar/{calendar-idx}")
    public ResponseEntity<ResponseObjectDTO> deleteCalendar(
            @PathVariable(value = "calendar-idx") Long calendarIdx) throws Exception {
        CalendarVO calendar = calendarService.getByIdx(calendarIdx);
        UserVO user = userService.getByAuth();

        if(calendar.getUser() != user)
            throw new AuthException("not owned calendar");

        calendarService.delete(calendar.getIdx());
        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);
    }





}
