package com.limbae.pfy.repository.etc;

import com.limbae.pfy.domain.etc.PositionVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<PositionVO, Long> {

    Optional<PositionVO> findOneByIdx(Long idx);
}
