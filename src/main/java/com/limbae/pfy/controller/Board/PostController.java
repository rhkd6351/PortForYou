package com.limbae.pfy.controller.Board;

import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.dto.board.BoardDTO;
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
public class PostController {

    BoardServiceInterface boardService;
    PostServiceInterface postService;
    UserService userService;
    StudyService studyService;
    EntityUtil entityUtil;


    public PostController(BoardServiceInterface boardService, PostServiceInterface postService, UserService userService, StudyService studyService, EntityUtil entityUtil) {
        this.boardService = boardService;
        this.postService = postService;
        this.userService = userService;
        this.studyService = studyService;
        this.entityUtil = entityUtil;
    }

    @GetMapping("/study/board/{board-idx}/posts")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<List<PostDTO>> getPosts(
            @PathVariable(value = "board-idx") Long boardIdx) throws Exception {
        BoardVO board = boardService.getByIdx(boardIdx);
        studyService.memberCheck(board.getStudy().getIdx());
        List<PostVO> posts = postService.getByBoardIdx(board.getIdx());

        List<PostDTO> postDTOS = posts.stream().map(entityUtil::convertPostVoToDto).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOS);
    }

    @GetMapping("/study/board/post/{post-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<PostDTO> getPost(
            @PathVariable(value = "post-idx") Long postIdx) throws Exception {
        PostVO post = postService.getByIdx(postIdx);
        studyService.memberCheck(post.getBoard().getStudy().getIdx());

        return ResponseEntity.ok(entityUtil.convertPostVoToDto(post));
    }

    @PostMapping("/study/board/{board-idx}/post")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<PostDTO> savePost(
            @PathVariable(value = "board-idx") Long boardIdx,
            @RequestBody PostDTO postDTO) throws Exception{

        BoardVO board = boardService.getByIdx(boardIdx);
        studyService.memberCheck(board.getStudy().getIdx());
        UserVO user = userService.getMyUserWithAuthorities();

        PostVO post = null;
        if(postDTO.getIdx() != null){
            post = postService.getByIdx(postDTO.getIdx());
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
        }else{
            post = PostVO.builder()
                    .idx(postDTO.getIdx()) //it can be null: create
                    .title(postDTO.getTitle())
                    .content(postDTO.getContent())
                    .board(board)
                    .user(user)
                    .build();
        }

        postService.update(post);
        return new ResponseEntity<>(entityUtil.convertPostVoToDto(post), HttpStatus.CREATED);
    }

    @DeleteMapping("/study/board/post/{post-idx}")
    @PreAuthorize("hasAnyRole({'USER','ADMIN'})")
    public ResponseEntity<ResponseObjectDTO> deletePost(
            @PathVariable(value = "post-idx") Long postIdx) throws Exception {
        PostVO post = postService.getByIdx(postIdx);
        UserVO user = userService.getMyUserWithAuthorities();
        if(!(user == post.getUser()))
            throw new AuthException("not owned post");

        postService.deleteByIdx(post.getIdx());
        return new ResponseEntity<>(new ResponseObjectDTO("delete success"), HttpStatus.NO_CONTENT);
    }




}














