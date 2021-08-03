package com.limbae.pfy.repository;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.StackVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StackRepository extends JpaRepository<StackVO, Long> {

    Optional<StackVO> findOneByIdx(Long idx);

    @Query("select s from StackVO s where s.name like concat('%',:query,'%')")
    List<StackVO> findByQuery(@Param(value = "query") String query);

}
