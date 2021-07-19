package com.limbae.pfy.repository;

import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.dto.StudyDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<StudyVO, Long> {

    List<StudyVO> findByUserUid(Long userUid);

}