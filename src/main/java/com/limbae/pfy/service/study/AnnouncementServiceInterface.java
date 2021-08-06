package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.StudyDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public interface AnnouncementServiceInterface {

    public AnnouncementVO getByIdx(Long idx) throws NotFoundException;

    public List<AnnouncementVO> getByStudyIdx(Long studyIdx) throws NotFoundException;

    public AnnouncementVO update(AnnouncementDTO dto) throws NotFoundException, AuthException;

    public AnnouncementVO update(AnnouncementVO announcementVO);

    public void delete(AnnouncementVO announcementVO);


    public Page<AnnouncementVO> getByQuery(String query, Pageable pageable);

    public Page<AnnouncementVO> getOrderByDesc(Pageable pageable);

    public Page<AnnouncementVO> getImminent(Pageable pageable);

    public List<AnnouncementVO> getAfterEndDate();

    public List<AnnouncementVO> getByPosition(PositionVO positionVO);
}
