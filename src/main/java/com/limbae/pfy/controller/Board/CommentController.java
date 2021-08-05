package com.limbae.pfy.controller.Board;

import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.domain.board.CommentVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.board.BoardDTO;
import com.limbae.pfy.dto.board.CommentDTO;
import com.limbae.pfy.dto.board.PostDTO;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@Slf4j
public class CommentController {

    BoardServiceInterface boardService;
    PostServiceInterface postService;
    CommentServiceInterface commentService;
    UserService userService;
    StudyService studyService;
    EntityUtil entityUtil = new EntityUtil();

    public CommentController(BoardServiceInterface boardService, PostServiceInterface postService, CommentServiceInterface commentService, UserService userService, StudyService studyService) {
        this.boardService = boardService;
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.studyService = studyService;
    }

    @GetMapping("/study/board/post/{post-idx}/comments")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<List<CommentDTO>> getComments(
            @PathVariable(value = "post-idx") Long postIdx) throws Exception{

        PostVO post = postService.getByIdx(postIdx);
        studyService.memberCheck(post.getBoard().getStudy().getIdx());

        List<CommentVO> comments = commentService.getByPostIdx(postIdx);
        List<CommentDTO> commentDTOS = comments.stream().map(entityUtil::convertCommentVoToDto).collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOS);
    }

    @GetMapping("/study/board/post/comment/{comment-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<CommentDTO> getComment(
            @PathVariable(value = "comment-idx") Long commentIdx) throws Exception{
        CommentVO comment = commentService.getByIdx(commentIdx);
        studyService.memberCheck(comment.getPost().getBoard().getStudy().getIdx());

        return ResponseEntity.ok(entityUtil.convertCommentVoToDto(comment));
    }

    @PostMapping("/study/board/post/{post-idx}/comment")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<CommentDTO> saveComment(
            @PathVariable(value = "post-idx") Long postIdx,
            @RequestBody CommentDTO commentDTO) throws Exception{

        PostVO post = postService.getByIdx(postIdx);
        UserVO user = userService.getMyUserWithAuthorities();
        studyService.memberCheck(post.getBoard().getStudy().getIdx());

        CommentVO comment = CommentVO.builder()
                .idx(commentDTO.getIdx())
                .content(commentDTO.getContent())
                .user(user)
                .post(post)
                .build();

        commentService.update(comment);

        return new ResponseEntity<>(entityUtil.convertCommentVoToDto(comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/study/board/post/comment/{comment-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<ResponseObjectDTO> deleteComment(
            @PathVariable(value = "comment-idx") Long commentIdx) throws Exception {
        CommentVO comment = commentService.getByIdx(commentIdx);
        UserVO user = userService.getMyUserWithAuthorities();
        if(user != comment.getUser())
            throw new AuthException("not owned comment");

        commentService.deleteByIdx(comment.getIdx());

        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);
    }




}














