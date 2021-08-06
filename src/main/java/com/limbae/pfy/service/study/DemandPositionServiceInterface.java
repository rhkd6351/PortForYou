package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.dto.DemandPositionDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;


public interface DemandPositionServiceInterface {

    public DemandPositionVO getByIdx(Long idx) throws NotFoundException;

    public DemandPositionVO update(DemandPositionDTO dto) throws NotFoundException;
}
