package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.etc.StackVO;
import com.limbae.pfy.domain.etc.TechVO;
import com.limbae.pfy.dto.TechDTO;
import com.limbae.pfy.repository.TechRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TechService {

    @Autowired
    TechRepository techRepository;

    @Autowired
    StackService stackService;

    public TechVO getByIdx(Long idx) throws NotFoundException {

        Optional<TechVO> tech = techRepository.findByIdx(idx);

        if(tech.isEmpty())
            throw new NotFoundException("invalid tech idx");

        return tech.get();
    }

    public TechVO update(TechDTO dto) throws NotFoundException {

        StackVO stack = stackService.getByIdx(dto.getStackIdx());

        TechVO tech = null;

        if(dto.getIdx() != null) {
            tech = this.getByIdx(dto.getIdx());
            tech.setContent(dto.getContent());
            tech.setAbility(dto.getAbility());
            tech.setStack(stack);

        }else{
            tech = TechVO.builder()
                    .content(dto.getContent())
                    .ability(dto.getAbility())
                    .stack(stack)
                    .build();
        }

        return techRepository.save(tech);
    }
}
