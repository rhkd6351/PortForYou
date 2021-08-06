package com.limbae.pfy.service.board;

import com.limbae.pfy.domain.board.BoardVO;

import java.util.List;

public interface BoardServiceInterface {

    BoardVO update(BoardVO board) throws Exception;

    List<BoardVO> getByStudyIdx(Long studyIdx) throws Exception;

    BoardVO getByIdx(Long idx) throws Exception;

    boolean deleteByIdx(Long idx) throws Exception;

}
