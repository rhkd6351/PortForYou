package com.limbae.pfy.repository.study;

import com.limbae.pfy.domain.study.MemberVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberVO, Long> {

}
