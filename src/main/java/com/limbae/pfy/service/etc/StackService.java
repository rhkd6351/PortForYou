package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.etc.StackVO;
import com.limbae.pfy.repository.etc.StackRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackService {

    @Autowired
    StackRepository stackRepository;

    public List<StackVO> getAll(){
        return stackRepository.findAll();
    }

    public StackVO getByIdx(Long idx) throws NotFoundException {
        Optional<StackVO> stack = stackRepository.findById(idx);
        if(stack.isEmpty())
            throw new NotFoundException("invalid stack idx");
        return stack.get();
    }

    public List<StackVO> getByQuery(String query){
        return stackRepository.findByQuery(query);
    }
}
