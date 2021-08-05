package com.limbae.pfy.service;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.dto.PortfolioDTO;

import java.util.List;

public interface PortfolioServiceInterface {

    public PortfolioVO getPortfolioByIdx(Long idx) throws Exception;

    public void deletePortfolio(PortfolioVO vo) throws Exception;

    public PortfolioVO updatePortfolio(PortfolioDTO portfolioDTO) throws Exception;

    public List<PortfolioVO> getPortfoliosByUid(Long uid) throws Exception;

}
