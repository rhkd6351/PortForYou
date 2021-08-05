package com.limbae.pfy.service;

import com.limbae.pfy.domain.board.CommentVO;

import java.util.List;

public interface CommentServiceInterface {

    CommentVO update(CommentVO comment) throws Exception;

    List<CommentVO> getByPostIdx(Long postIdx) throws Exception;

    CommentVO getByIdx(Long idx) throws Exception;

    boolean deleteByIdx(Long idx) throws Exception;

}
