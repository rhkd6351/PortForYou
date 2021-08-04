package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.AnnouncementDTO;
import com.limbae.pfy.repository.AnnouncementRepository;
import com.limbae.pfy.repository.DemandPositionRepository;
import com.limbae.pfy.repository.StudyRepository;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.util.EntityUtil;
import com.limbae.pfy.util.SecurityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DemandPositionService {

    @Autowired
    DemandPositionRepository demandPositionRepository;

    public List<DemandPositionVO> getByPosition(PositionVO position){
        return demandPositionRepository.findByPosition(position);
    }

}
