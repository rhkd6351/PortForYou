package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.BoardVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardVO, Long> {


}
