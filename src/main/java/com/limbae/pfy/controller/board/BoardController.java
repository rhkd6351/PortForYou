package com.limbae.pfy.controller.board;

import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.dto.board.BoardDTO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.dto.etc.ResponseObjectDTO;
import com.limbae.pfy.service.board.BoardServiceInterface;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Slf4j
public class BoardController {

    BoardServiceInterface boardService;
    StudyServiceInterfaceImpl studyService;
    EntityUtil entityUtil;

    public BoardController(BoardServiceInterface boardService, StudyServiceInterfaceImpl studyService, EntityUtil entityUtil) {
        this.boardService = boardService;
        this.studyService = studyService;
        this.entityUtil = entityUtil;
    }

    @GetMapping("/study/{study-idx}/boards")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<List<BoardDTO>> getBoardList(
            @PathVariable(value = "study-idx") Long studyIdx) throws Exception{

        studyService.memberCheck(studyIdx);

        List<BoardVO> boards = boardService.getByStudyIdx(studyIdx);
        List<BoardDTO> boardDTOS = boards.stream().map(entityUtil::convertBoardVoToDto).collect(Collectors.toList());

        return ResponseEntity.ok(boardDTOS);
    }

    @GetMapping("/study/board/{board-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<BoardDTO> getBoard(
            @PathVariable(value = "board-idx") Long boardIdx) throws Exception {

        BoardVO board = boardService.getByIdx(boardIdx);
        studyService.memberCheck(board.getStudy().getIdx());

        BoardDTO boardDTO = entityUtil.convertBoardVoToDto(board);

        return ResponseEntity.ok(boardDTO);
    }

    @PostMapping("/study/{study-idx}/board")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<BoardDTO> saveBoard(
            @PathVariable(value = "study-idx") Long studyIdx,
            @RequestBody BoardDTO boardDTO) throws Exception{
        StudyVO study = studyService.getByIdx(studyIdx);
        studyService.memberCheck(study.getIdx());

        BoardVO board = null;
        if(boardDTO.getIdx() != null){
            board = boardService.getByIdx(boardDTO.getIdx());
            board.setName(boardDTO.getName());
            board.setContent(boardDTO.getContent());
        }
        else{
            board = BoardVO.builder()
                    .idx(boardDTO.getIdx()) //it can be null: create
                    .name(boardDTO.getName())
                    .content(boardDTO.getContent())
                    .study(study)
                    .build();
        }

        boardService.update(board);
        return new ResponseEntity<>(entityUtil.convertBoardVoToDto(board), HttpStatus.CREATED);
    }

    @DeleteMapping("/study/board/{board-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<ResponseObjectDTO> deleteBoard(
            @PathVariable(value = "board-idx") Long boardIdx) throws Exception {

        BoardVO board = boardService.getByIdx(boardIdx);
        studyService.managerCheck(board.getStudy().getIdx());

        boardService.deleteByIdx(board.getIdx());

        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);
    }

}














