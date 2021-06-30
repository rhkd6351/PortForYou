package com.limbae.pfy.repository;

import com.limbae.pfy.domain.ProjectVO;
import com.limbae.pfy.domain.StackVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void test_() {
        //given
        int uid = 1;
        //when
        Optional<ProjectVO> pvo = projectRepository.findOneWithStackByUid(uid);
        List<StackVO> stack = pvo.get().getStack();
        //then
        stack.forEach(i -> System.out.println("i = " + i.getName()));
    }
}
