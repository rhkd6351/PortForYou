package com.limbae.pfy.service.user;

import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.dto.user.PortfolioDTO;

import java.util.List;

public interface PortfolioServiceInterface {

    public PortfolioVO getByIdx(Long idx) throws Exception;

    public void delete(PortfolioVO vo) throws Exception;

    public PortfolioVO update(PortfolioDTO portfolioDTO) throws Exception;

    public List<PortfolioVO> getByUid(Long uid) throws Exception;

}
