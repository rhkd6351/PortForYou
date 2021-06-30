package com.limbae.pfy.repository;

import com.limbae.pfy.domain.UiImageVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UiImageRepository extends JpaRepository<UiImageVO, Long> {

    Optional<UiImageVO> findOneByName(String name);
}
