package com.limbae.pfy.service.board;

import com.limbae.pfy.domain.board.PostVO;

import java.util.List;

public interface PostServiceInterface {

    PostVO update(PostVO post) throws Exception;

    List<PostVO> getByBoardIdx(Long boardIdx) throws Exception;

    PostVO getByIdx(Long idx) throws Exception;

    boolean deleteByIdx(Long idx) throws Exception;

}
