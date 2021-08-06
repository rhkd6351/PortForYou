package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.etc.EducationVO;
import com.limbae.pfy.repository.EducationRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EducationService {

    @Autowired
    EducationRepository educationRepository;

    public List<EducationVO> getAll(){
        return educationRepository.findAll();
    }

    public EducationVO getByIdx(Long idx) throws NotFoundException {
        Optional<EducationVO> education = educationRepository.findByIdx(idx);
        if(education.isEmpty())
            throw new NotFoundException("invalid education idx");
        return education.get();
    }

}
