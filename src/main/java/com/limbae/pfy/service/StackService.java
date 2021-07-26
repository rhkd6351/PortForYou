package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.repository.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StackService {

    @Autowired
    StackRepository stackRepository;

    public List<StackVO> getStackList(){
        return stackRepository.findAll();
    }

    public StackVO getStackByIdx(Long idx){
        return stackRepository.getById(idx);
    }

}
