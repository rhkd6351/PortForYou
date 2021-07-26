package com.limbae.pfy.repository;

import com.limbae.pfy.domain.AnnouncementVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<AnnouncementVO, Long> {

    public List<AnnouncementVO> findByStudyIdx(Long studyIdx);

    public List<AnnouncementVO> findTop50ByOrderByIdxDesc();

}
