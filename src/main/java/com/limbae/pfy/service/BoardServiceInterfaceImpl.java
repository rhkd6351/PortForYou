package com.limbae.pfy.service;

import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.repository.BoardRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceInterfaceImpl implements BoardServiceInterface{

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    StudyService studyService;

    @Override
    public BoardVO update(BoardVO board){
        return boardRepository.save(board);
    }

    @Override
    public List<BoardVO> getByStudyIdx(Long studyIdx) throws Exception {
        StudyVO study = studyService.getStudyByIdx(studyIdx);
        return boardRepository.getAllByStudyIdx(study.getIdx());
    }

    @Override
    public BoardVO getByIdx(Long idx) throws Exception {
        Optional<BoardVO> board = boardRepository.getByIdx(idx);
        if(board.isEmpty())
            throw new NotFoundException("invalid board idx");

        return board.get();
    }

    @Override
    public boolean deleteByIdx(Long idx) throws Exception {
        BoardVO board = this.getByIdx(idx);
        try{
            boardRepository.delete(board);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
