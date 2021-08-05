package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.CommentVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentVO, Long> {

    public List<CommentVO> getByPostIdx(Long postIdx);

    public Optional<CommentVO> getByIdx(Long idx);

}
