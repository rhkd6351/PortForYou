package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.study.StudyCategoryVO;
import com.limbae.pfy.repository.study.StudyCategoryRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyCategoryService {

    @Autowired
    StudyCategoryRepository studyCategoryRepository;

    public List<StudyCategoryVO> getAll(){
        return studyCategoryRepository.findAll();
    }

    public StudyCategoryVO getByIdx(Long idx) throws NotFoundException {
        Optional<StudyCategoryVO> studyCategory = studyCategoryRepository.findById(idx);

        if(studyCategory.isEmpty())
            throw new NotFoundException("invalid studyCategory idx");

        return studyCategory.get();
    }

}
