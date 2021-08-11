package com.limbae.pfy.repository.user;

import com.limbae.pfy.domain.user.UserVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserVO, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserVO> findWithAuthoritiesByUsername(String username);

    Optional<UserVO> findByUid(Long uid);

    @EntityGraph(attributePaths = "study")
    Optional<UserVO> findOneWithStudyByUsername(String username);

    @EntityGraph(attributePaths = {"portfolio"})
    Optional<UserVO> findOneWithPortfolioByUsername(String username);

    @EntityGraph(attributePaths = {"portfolio"})
    Optional<UserVO> findOneWithPortfolioByUid(Long uid);
}
