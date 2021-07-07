package com.limbae.pfy.service;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.PortfolioDTO;
import com.limbae.pfy.dto.PortfolioListDto;
import com.limbae.pfy.repository.PortfolioRepository;
import com.limbae.pfy.repository.PositionRepository;
import com.limbae.pfy.repository.StackRepository;
import com.limbae.pfy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StackService {

    @Autowired
    StackRepository stackRepository;

    public List<StackVO> getStackList(){
        return stackRepository.findAll();
    }

}
