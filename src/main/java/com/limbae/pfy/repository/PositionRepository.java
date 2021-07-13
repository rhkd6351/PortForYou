package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.PositionVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<PositionVO, Long> {

    Optional<PositionVO> findOneByIdx(Long idx);
}
