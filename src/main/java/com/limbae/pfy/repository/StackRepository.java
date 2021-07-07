package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.StackVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StackRepository extends JpaRepository<StackVO, Long> {

    Optional<StackVO> findOneByIdx(Long idx);

}
