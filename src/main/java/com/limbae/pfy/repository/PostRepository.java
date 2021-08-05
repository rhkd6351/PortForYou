package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostVO, Long> {

    public List<PostVO> getByBoardIdx(Long boardIdx);

    public Optional<PostVO> getByIdx(Long idx);

}
