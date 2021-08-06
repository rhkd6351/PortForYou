package com.limbae.pfy.repository;

import com.limbae.pfy.domain.etc.ImageVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageVO, Long> {

    Optional<ImageVO> findOneByName(String name);
}
