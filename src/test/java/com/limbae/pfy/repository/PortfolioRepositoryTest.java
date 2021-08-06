//package com.limbae.pfy.repository;
//
//import com.limbae.pfy.domain.user.PortfolioVO;
//import com.limbae.pfy.domain.etc.PositionVO;
//import com.limbae.pfy.domain.etc.ProjectVO;
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
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//public class PortfolioRepositoryTest {
//
//    @Autowired
//    PortfolioRepository portfolioRepository;
//
//    @Test
//    void test_1() {
//        //given
//        Optional<PortfolioVO> pvo = portfolioRepository.findOneWithProjectByIdx(18L);
//
//        //when
//        List<ProjectVO> list = new ArrayList<>(pvo.get().getProject());
//
//        //then
//        Assertions.assertThat(list).isNotEmpty();
//        list.forEach(i -> System.out.println(i.getTitle()));
//
//    }
////
//////    @Test
//////    void test_2() {
//////        //given
//////        Optional<PortfolioVO> pvo = portfolioRepository.findOneByIdx(3);
//////
//////        //when
//////        Set<PositionVO> positions = pvo.get().getPositions();
//////        List<ProjectVO> project = pvo.get().getProject();
//////
//////        //then
//////        for(PositionVO vo : positions){
//////            System.out.println("vo = " + vo.getName());
//////        }
//////        for(ProjectVO vo : project){
//////            System.out.println("vo.getTitle() = " + vo.getTitle());
//////        }
//////    }
//}
