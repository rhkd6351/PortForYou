package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.repository.etc.PositionRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    PositionRepository positionRepository;

    public List<PositionVO> getAll(){
        return positionRepository.findAll();
    }

    public PositionVO getByIdx(Long idx) throws NotFoundException {
        Optional<PositionVO> position = positionRepository.findById(idx);
        if(position.isEmpty())
            throw new NotFoundException("invalid position idx");
        return position.get();
    }

}
