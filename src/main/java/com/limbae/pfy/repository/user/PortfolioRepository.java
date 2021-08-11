package com.limbae.pfy.repository.user;

import com.limbae.pfy.domain.user.PortfolioVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<PortfolioVO, Long> {

    @EntityGraph(attributePaths = {"project", "position", "tech", "education"})
    Optional<PortfolioVO> findOneByIdx(Long idx);

    @EntityGraph(attributePaths = {"tech", "position"})
    List<PortfolioVO> findByUserUid(Long uid);
}
