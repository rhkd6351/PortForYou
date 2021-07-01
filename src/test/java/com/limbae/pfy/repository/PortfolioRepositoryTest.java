//package com.limbae.pfy.repository;
//
//import com.limbae.pfy.domain.PortfolioVO;
//import com.limbae.pfy.domain.ProjectVO;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.sql.SQLOutput;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//public class PortfolioRepositoryTest {
//
//    @Autowired
//    PortfolioRepository portfolioRepository;
//
//    @Test
//    void test_() {
//        //given
//        Optional<PortfolioVO> pvo = portfolioRepository.findOneWithProjectByIdx(3);
//
//        //when
//        List<ProjectVO> list = new ArrayList<>(pvo.get().getProject());
//
//        //then
//        Assertions.assertThat(list).isNotEmpty();
//        list.forEach(i -> System.out.println(i.getTitle()));
//
//    }
//}
