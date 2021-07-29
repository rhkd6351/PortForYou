package com.limbae.pfy.repository;

import com.limbae.pfy.domain.ProjectVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectVO, Long> {

}
