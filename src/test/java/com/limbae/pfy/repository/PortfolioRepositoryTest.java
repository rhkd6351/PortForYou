package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.ProjectVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class PortfolioRepositoryTest {

    @Autowired
    PortfolioRepository portfolioRepository;

    @Test
    void test_() {
        //given

        //when
        Optional<PortfolioVO> pvo = portfolioRepository.findOneWithProjectByUid(3);

        //then
        List<ProjectVO> list = new ArrayList<>(pvo.get().getProject());
        list.forEach(i -> System.out.println(i.getTitle()));
    }
}
