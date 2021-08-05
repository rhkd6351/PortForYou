package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.CommentVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentVO, Long> {

}
