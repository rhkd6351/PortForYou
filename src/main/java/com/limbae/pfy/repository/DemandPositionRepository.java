package com.limbae.pfy.repository;

import com.limbae.pfy.domain.DemandPositionVO;
import com.limbae.pfy.domain.PositionVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandPositionRepository extends JpaRepository<DemandPositionVO, Long> {

    @Query("select d from DemandPositionVO d where d.position = :position")
    public List<DemandPositionVO> findByPosition(@Param(value = "position") PositionVO position);

}
