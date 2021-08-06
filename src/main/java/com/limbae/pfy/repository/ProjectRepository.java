package com.limbae.pfy.repository;

import com.limbae.pfy.domain.etc.ProjectVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectVO, Long> {

    public Optional<ProjectVO> findByIdx(Long idx);

}
