package com.limbae.pfy.service;

import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.repository.PostRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceInterfaceImpl implements PostServiceInterface{

    @Autowired
    PostRepository postRepository;

    @Autowired
    BoardServiceInterface boardServiceInterface;

    @Override
    public PostVO update(PostVO post) throws Exception {
        return postRepository.save(post);
    }

    @Override
    public List<PostVO> getByBoardIdx(Long boardIdx) throws Exception {
        BoardVO board = boardServiceInterface.getByIdx(boardIdx);
        return postRepository.getByBoardIdx(board.getIdx());
    }

    @Override
    public PostVO getByIdx(Long idx) throws Exception {
        Optional<PostVO> post = postRepository.getByIdx(idx);
        if(post.isEmpty())
            throw new NotFoundException("invalid post idx");

        return post.get();
    }

    @Override
    public boolean deleteByIdx(Long idx) throws Exception {
        PostVO post = this.getByIdx(idx);
        try{
            postRepository.delete(post);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
