package com.limbae.pfy.service;

import com.limbae.pfy.jwt.TokenProvider;
import com.limbae.pfy.repository.PortfolioRepository;
import com.limbae.pfy.repository.UserRepository;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@SpringBootTest
public class PortfolioServiceTest {

    PortfolioRepository portfolioRepository;
    PortfolioServiceInterfaceImpl portfolioService;
    UserRepository userRepository;
    AuthenticationManagerBuilder authenticationManagerBuilder;
    TokenProvider tokenProvider;
    EntityUtil entityUtil;

    @Autowired
    public PortfolioServiceTest(PortfolioRepository portfolioRepository, PortfolioServiceInterfaceImpl portfolioService, UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, EntityUtil entityUtil) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.entityUtil = entityUtil;
    }


//    @Test
//    void getOnePortfolioTest() {
//        //given
//        Optional<PortfolioVO> portfolioVO = portfolioRepository.findAll().stream().findAny();
//
//        //when
//        Optional<PortfolioVO> portfolioByIdx = portfolioService.getByIdx(portfolioVO.get().getIdx());
//
//        //then
//        Assertions.assertThat(portfolioByIdx).isNotEmpty();
//        System.out.println("portfolioByIdx.get().getIdx() = " + portfolioByIdx.get().getIdx());
//        portfolioByIdx.get().setContent("wow22");
//        portfolioRepository.flush();
//
//
//    }
//
//    @Test
//    void getPortfolioListTest() {
//        //given
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken("yekwang2", "yekwang2");
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        //when
//        List<PortfolioListDTO> myPortfolios = portfolioService.getMyPortfolios();
//
//        //then
//        Assertions.assertThat(myPortfolios).isNotEmpty();
//    }
//
//    @DisplayName("posession")
//    @Test
//    void possesionOfPortfolioTest() {
//        //given
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken("yekwang2", "yekwang2");
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        List<PortfolioListDTO> myPortfolios = portfolioService.getMyPortfolios();
//        PortfolioVO portfolioByIdx = portfolioService.getByIdx(myPortfolios.stream().findAny().get().getIdx()).get();
//
//        //when
//        boolean check = portfolioService.checkPossessionOfPortfolio(portfolioByIdx, myPortfolios);
//
//        //then
//        Assertions.assertThat(check).isTrue();
//    }
//
//    @Test
//    void savePortfolioTest() {
//        //given
//
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken("admin", "admin");
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Set<ProjectDTO> projectDTOSet = new HashSet<>();
//        projectDTOSet.add(ProjectDTO.builder()
//                .title("dummy title")
//                .content("dummy")
//                .build());
//
//        EducationDTO education = EducationDTO.builder().idx(1L).name("학부 졸업").build();
//
//        PortfolioDTO dto = PortfolioDTO.builder()
//                .title("title")
//                .content("content")
//                .project(projectDTOSet)
//                .education(education)
//                .build();
//
//        //when
//        PortfolioVO portfolioVO = portfolioService.savePortfolio(dto);
//
//        //then
//        Assertions.assertThat(portfolioVO).isNotNull();
//        Assertions.assertThat(portfolioVO.getProject()).isNotNull();
//    }
//
//    @Test
//    void updatePortfolioTest() {
//        //given
//
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken("admin", "admin");
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        Optional<PortfolioVO> portfolioByIdx = portfolioService.getByIdx(12L);
//
//        //when
//        portfolioByIdx.get().setTitle("changed title");
//        portfolioService.updatePortfolio(entityUtil.convertPortfolioVoToDto(portfolioByIdx.get()));
//        Optional<PortfolioVO> portfolioByIdx1 = portfolioService.getByIdx(12L);
//
//        //then
//        Assertions.assertThat(portfolioByIdx1.get().getTitle()).isEqualTo("changed title");
//    }
}
