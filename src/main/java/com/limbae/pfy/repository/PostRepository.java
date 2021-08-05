package com.limbae.pfy.repository;

import com.limbae.pfy.domain.board.PostVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostVO, Long> {


}
