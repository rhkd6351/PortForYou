package com.limbae.pfy.repository;

import com.limbae.pfy.domain.StudyApplicationVO;
import com.limbae.pfy.domain.StudyVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.NamedSubgraph;
import java.util.List;
import java.util.Optional;

public interface StudyApplicationRepository extends JpaRepository<StudyApplicationVO, Long> {


    @EntityGraph(attributePaths = {"position", "announcement", "portfolio"})
    List<StudyApplicationVO> findByAnnouncementIdx(Long studyAnnouncementIdx);

}
