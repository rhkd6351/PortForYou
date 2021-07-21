package com.limbae.pfy.repository;

import com.limbae.pfy.domain.StudyCategoryVO;
import com.limbae.pfy.domain.StudyVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyCategoryRepository extends JpaRepository<StudyCategoryVO, Long> {

}
