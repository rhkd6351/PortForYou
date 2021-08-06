package com.limbae.pfy.repository;

import com.limbae.pfy.domain.study.StudyVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<StudyVO, Long> {

    List<StudyVO> findByUserUid(Long userUid);

    @EntityGraph(attributePaths = "members")
    List<StudyVO> findWithMembersByUserUid(Long userUid);

    @EntityGraph(attributePaths = "announcements")
    Optional<StudyVO> findWithAnnouncementsByIdx(Long idx);

}
