package com.limbae.pfy.service;

import com.limbae.pfy.domain.StackVO;
import com.limbae.pfy.domain.StudyCategoryVO;
import com.limbae.pfy.repository.StackRepository;
import com.limbae.pfy.repository.StudyCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyCategoryService {

    @Autowired
    StudyCategoryRepository studyCategoryRepository;

    public List<StudyCategoryVO> getCategoryList(){
        return studyCategoryRepository.findAll();
    }

}
