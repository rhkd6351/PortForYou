package com.limbae.pfy.service.board;

import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.repository.BoardRepository;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
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
    StudyServiceInterfaceImpl studyService;

    @Override
    public BoardVO update(BoardVO board){
        return boardRepository.save(board);
    }

    @Override
    public List<BoardVO> getByStudyIdx(Long studyIdx) throws Exception {
        StudyVO study = studyService.getByIdx(studyIdx);
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
    public void deleteByIdx(Long idx) throws Exception {
        BoardVO board = this.getByIdx(idx);
        try{
            boardRepository.delete(board);
        }catch (Exception e){
        }
    }

}
