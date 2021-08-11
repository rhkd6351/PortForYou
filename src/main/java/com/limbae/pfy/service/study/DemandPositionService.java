package com.limbae.pfy.service.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.dto.study.DemandPositionDTO;
import com.limbae.pfy.repository.study.DemandPositionRepository;
import com.limbae.pfy.service.etc.PositionService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DemandPositionService implements DemandPositionServiceInterface {

    @Autowired
    DemandPositionRepository demandPositionRepository;

    @Autowired
    PositionService positionService;

    public DemandPositionVO getByIdx(Long idx) throws NotFoundException {
        Optional<DemandPositionVO> demandPosition = demandPositionRepository.findById(idx);
        if(demandPosition.isEmpty())
            throw new NotFoundException("invalid demandPosition idx");

        return demandPosition.get();
    }

    public DemandPositionVO update(DemandPositionDTO dto) throws NotFoundException {
        PositionVO position = positionService.getByIdx(dto.getPosition().getIdx());

        DemandPositionVO demandPosition = null;
        if(dto.getIdx() != null){
            demandPosition = this.getByIdx(dto.getIdx());
            demandPosition.setPosition(position);
            demandPosition.setDemand(dto.getDemand());
            demandPosition.setApplied(dto.getApplied());
        }else{
            demandPosition = DemandPositionVO.builder()
                    .position(position)
                    .demand(dto.getDemand())
                    .build();
        }
        return demandPositionRepository.save(demandPosition);
    }

}
