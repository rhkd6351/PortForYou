package com.limbae.pfy.service;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.StackVO;
import com.limbae.pfy.repository.PositionRepository;
import com.limbae.pfy.repository.StackRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    @Autowired
    PositionRepository positionRepository;

    public List<PositionVO> getPositionList(){
        return positionRepository.findAll();
    }

    public PositionVO getPositionByIdx(Long idx) throws NotFoundException {
        Optional<PositionVO> position = positionRepository.findById(idx);
        if(position.isEmpty())
            throw new NotFoundException("invalid position idx");
        return position.get();


    }

}
