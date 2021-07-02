package com.limbae.pfy.service;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.repository.PortfolioRepository;
import com.limbae.pfy.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public Optional<PortfolioVO> getPortfolioByIdx(int idx){
        return portfolioRepository.findOneWithProjectAndPositionByIdx(idx);
    }

}
