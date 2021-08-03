package com.limbae.pfy.repository;

import com.limbae.pfy.domain.AnnouncementVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<AnnouncementVO, Long> {

    public List<AnnouncementVO> findByStudyIdx(Long studyIdx);

    public List<AnnouncementVO> findTop50ByOrderByIdxDesc();

//    public List<AnnouncementVO> findAllByTitleLike(String title, String content, Pageable pageable);

    @Query("select n from AnnouncementVO as n where n.title like concat('%',:query,'%') OR n.content like concat('%',:query,'%') order by n.idx desc")
    public List<AnnouncementVO> findByQuery(@Param(value = "query") String query, Pageable pageable);

    

}
