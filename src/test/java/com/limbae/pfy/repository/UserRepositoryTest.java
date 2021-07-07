package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.ProjectVO;
import com.limbae.pfy.domain.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Test
    void test_() {
        //given
        Optional<UserVO> vo = userRepository.findOneWithPortfolioByUsername("kwang");
        UserVO vo2;
        PortfolioVO pvo = null;

        //when
        if(vo.isPresent()){
            vo2 = vo.get();
            for (PortfolioVO pf:vo2.getPortfolio()) {
                System.out.print(pf.getIdx() + " : ");
                System.out.println(pf.getTitle());
                if(pf.getIdx() == 3)
                    pvo = pf;
            }
        }

        pvo = portfolioRepository.findOneWithProjectByIdx(pvo.getIdx()).get();


        for(ProjectVO pj : pvo.getProject()){
            System.out.println("pj.getTitle() = " + pj.getTitle());
        }

    }
}
