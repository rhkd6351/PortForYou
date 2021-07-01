//package com.limbae.pfy.repository;
//
//import com.limbae.pfy.domain.PortfolioVO;
//import com.limbae.pfy.domain.UserVO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.sound.sampled.Port;
//import java.util.Optional;
//
//@SpringBootTest
//public class UserRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    void test_() {
//        //given
//        Optional<UserVO> vo = userRepository.findOneWithPortfolioByUsername("kwang");
//        UserVO vo2 = null;
//        PortfolioVO pvo = null;
//
//        //when
//        if(vo.isPresent()){
//            vo2 = vo.get();
//            for (PortfolioVO pf:vo2.getPortfolio()) {
//                System.out.println(pf.getTitle()); }
//        }
//        //then
//        pvo = PortfolioVO.builder()
//                .title("test title")
//                .content("test content")
//                .user_uid(vo2.getUserId())
//                .build();
//
//        vo2.getPortfolio().add(pvo);
//        userRepository.save(vo2);
//
//
//    }
//}
