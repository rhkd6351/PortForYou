package com.limbae.pfy.repository;


import com.limbae.pfy.domain.UiImageVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UiImageRepositoryTest {

    @Autowired
    UiImageRepository uiImageRepository;

    @Test
    void test_() {
        //given
        String name = "login-logo";
        //when

        Optional<UiImageVO> uvo = uiImageRepository.findOneByName("login-logo");

        //then
        System.out.println(uvo.get().getUploadPath());
    }
}
