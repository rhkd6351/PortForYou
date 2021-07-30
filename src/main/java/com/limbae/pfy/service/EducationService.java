package com.limbae.pfy.service;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.EducationVO;
import com.limbae.pfy.repository.AnnouncementRepository;
import com.limbae.pfy.repository.EducationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EducationService {

    @Autowired
    EducationRepository educationRepository;

    public List<EducationVO> getEducationList(){
        return educationRepository.findAll();
    }

}
