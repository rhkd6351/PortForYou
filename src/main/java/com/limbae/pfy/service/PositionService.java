package com.limbae.pfy.service;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.StackVO;
import com.limbae.pfy.repository.PositionRepository;
import com.limbae.pfy.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    PositionRepository positionRepository;

    public List<PositionVO> getPositionList(){
        return positionRepository.findAll();
    }

}
