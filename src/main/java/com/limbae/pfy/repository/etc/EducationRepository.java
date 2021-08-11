package com.limbae.pfy.repository.etc;

import com.limbae.pfy.domain.etc.EducationVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationRepository extends JpaRepository<EducationVO, Long> {

    public Optional<EducationVO> findByIdx(Long idx);

}
