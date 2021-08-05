package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.BoardVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<BoardVO, Long> {

    List<BoardVO> getAllByStudyIdx(Long studyIdx);

    Optional<BoardVO> getByIdx(Long idx);


}
