package com.limbae.pfy.controller.board;

import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.domain.board.CommentVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.dto.etc.ResponseObjectDTO;
import com.limbae.pfy.dto.board.CommentDTO;
import com.limbae.pfy.service.board.BoardServiceInterface;
import com.limbae.pfy.service.board.CommentServiceInterface;
import com.limbae.pfy.service.board.PostServiceInterface;
import com.limbae.pfy.service.study.StudyServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
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
    UserServiceInterfaceImpl userService;
    StudyServiceInterfaceImpl studyService;
    EntityUtil entityUtil;

    public CommentController(BoardServiceInterface boardService, PostServiceInterface postService, CommentServiceInterface commentService, UserServiceInterfaceImpl userService, StudyServiceInterfaceImpl studyService, EntityUtil entityUtil) {
        this.boardService = boardService;
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
        this.studyService = studyService;
        this.entityUtil = entityUtil;
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
        UserVO user = userService.getByAuth();
        studyService.memberCheck(post.getBoard().getStudy().getIdx());

        CommentVO comment = null;
        if(commentDTO.getIdx() != null){
            comment = commentService.getByIdx(commentDTO.getIdx());
            comment.setContent(commentDTO.getContent());
        } else{
            comment = CommentVO.builder()
                    .idx(commentDTO.getIdx())
                    .content(commentDTO.getContent())
                    .user(user)
                    .post(post)
                    .build();
        }

        commentService.update(comment);

        return new ResponseEntity<>(entityUtil.convertCommentVoToDto(comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/study/board/post/comment/{comment-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<ResponseObjectDTO> deleteComment(
            @PathVariable(value = "comment-idx") Long commentIdx) throws Exception {
        CommentVO comment = commentService.getByIdx(commentIdx);
        UserVO user = userService.getByAuth();
        if(user != comment.getUser())
            throw new AuthException("not owned comment");

        commentService.deleteByIdx(comment.getIdx());

        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);
    }




}














