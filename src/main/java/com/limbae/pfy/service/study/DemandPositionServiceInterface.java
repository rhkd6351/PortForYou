package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.dto.study.DemandPositionDTO;
import javassist.NotFoundException;


public interface DemandPositionServiceInterface {

    public DemandPositionVO getByIdx(Long idx) throws NotFoundException;

    public DemandPositionVO update(DemandPositionDTO dto) throws NotFoundException;
}
