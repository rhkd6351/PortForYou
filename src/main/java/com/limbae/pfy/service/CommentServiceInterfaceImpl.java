package com.limbae.pfy.service;

import com.limbae.pfy.domain.board.CommentVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.repository.CommentRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceInterfaceImpl implements CommentServiceInterface{

    @Autowired
    public CommentServiceInterfaceImpl(CommentRepository commentRepository, PostServiceInterface postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    CommentRepository commentRepository;
    PostServiceInterface postService;

    @Override
    public CommentVO update(CommentVO comment) throws Exception {
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentVO> getByPostIdx(Long postIdx) throws Exception {
        PostVO post = postService.getByIdx(postIdx);
        return commentRepository.getByPostIdx(post.getIdx());
    }

    @Override
    public CommentVO getByIdx(Long idx) throws Exception {
        Optional<CommentVO> comment = commentRepository.getByIdx(idx);
        if(comment.isEmpty())
            throw new NotFoundException("invalid comment idx");
        return comment.get();
    }

    @Override
    public boolean deleteByIdx(Long idx) throws Exception {
        CommentVO comment = this.getByIdx(idx);
        try{
            commentRepository.delete(comment);
            return true;
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
}
