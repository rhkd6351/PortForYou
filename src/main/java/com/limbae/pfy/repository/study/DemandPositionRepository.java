package com.limbae.pfy.repository.study;

import com.limbae.pfy.domain.study.DemandPositionVO;
import com.limbae.pfy.domain.etc.PositionVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandPositionRepository extends JpaRepository<DemandPositionVO, Long> {

    @Query("select d from DemandPositionVO d where d.position = :position")
    public List<DemandPositionVO> findByPosition(@Param(value = "position") PositionVO position);

}
