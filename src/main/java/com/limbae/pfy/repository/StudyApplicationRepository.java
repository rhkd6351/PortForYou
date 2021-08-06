package com.limbae.pfy.repository;

import com.limbae.pfy.domain.study.StudyApplicationVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyApplicationRepository extends JpaRepository<StudyApplicationVO, Long> {


    @EntityGraph(attributePaths = {"position", "announcement", "portfolio"})
    List<StudyApplicationVO> findByAnnouncementIdx(Long studyAnnouncementIdx);

}
