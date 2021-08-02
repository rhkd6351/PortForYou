package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.UserVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<PortfolioVO, Long> {

    @EntityGraph(attributePaths = {"project", "position", "tech", "education"})
    Optional<PortfolioVO> findOneByIdx(Long idx);

    @EntityGraph(attributePaths = {"tech", "position"})
    Optional<List<PortfolioVO>> findByUserUid(Long uid);
}
