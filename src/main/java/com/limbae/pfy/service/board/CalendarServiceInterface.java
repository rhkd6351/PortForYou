package com.limbae.pfy.service.board;

import com.limbae.pfy.domain.board.CalendarVO;
import com.limbae.pfy.dto.board.CalendarDTO;
import javassist.NotFoundException;

import javax.security.auth.message.AuthException;
import java.util.List;

public interface CalendarServiceInterface {


    CalendarVO update(CalendarDTO dto) throws AuthException, NotFoundException;

    CalendarVO update(CalendarVO vo);

    List<CalendarVO> getByStudyIdx(Long studyIdx) throws NotFoundException, AuthException;

    CalendarVO getByIdx(Long idx) throws NotFoundException;

    void delete(long idx) throws AuthException, NotFoundException, Exception;



}
