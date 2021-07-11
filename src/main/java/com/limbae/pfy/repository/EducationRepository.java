package com.limbae.pfy.repository;

import com.limbae.pfy.domain.EducationVO;
import com.limbae.pfy.domain.StackVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationRepository extends JpaRepository<EducationVO, Long> {

}
