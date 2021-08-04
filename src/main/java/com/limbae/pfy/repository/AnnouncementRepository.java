package com.limbae.pfy.repository;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.PositionVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<AnnouncementVO, Long> {

    public List<AnnouncementVO> findByStudyIdx(Long studyIdx);

    @EntityGraph(attributePaths = {"demandPosition"}, type = EntityGraph.EntityGraphType.LOAD)
    public Page<AnnouncementVO> findByOrderByIdxDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"demandPosition"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from AnnouncementVO as n where (n.title like concat('%',:query,'%') OR n.content like concat('%',:query,'%')) And n.activated = true order by n.idx desc")
    public Page<AnnouncementVO> findByQuery(@Param(value = "query") String query, Pageable pageable);

    @EntityGraph(attributePaths = {"demandPosition"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from AnnouncementVO n where n.activated = true order by n.endDate asc")
    public Page<AnnouncementVO> findByOrderByEndDateDesc(Pageable pageable);

    @Query("select n from AnnouncementVO n where n.endDate < current_timestamp And n.activated = true")
    public List<AnnouncementVO> findByAfterEndDate();

    @EntityGraph(attributePaths = {"demandPosition"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select distinct n from AnnouncementVO n join n.demandPosition d where d.position = :position")
    public List<AnnouncementVO> findByPosition(@Param(value = "position")PositionVO position);

    @Query("select count(n.idx) from AnnouncementVO n")
    public int getCount();


}
