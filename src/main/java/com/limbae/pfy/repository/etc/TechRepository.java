package com.limbae.pfy.repository.etc;

import com.limbae.pfy.domain.etc.TechVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechRepository extends JpaRepository<TechVO, Long> {

    public Optional<TechVO> findByIdx(Long idx);

}
