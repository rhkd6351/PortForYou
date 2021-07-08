package com.limbae.pfy.repository;

import com.limbae.pfy.domain.UserVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserVO, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserVO> findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = {"portfolio"})
    Optional<UserVO> findOneWithPortfolioByUsername(String username);
}
