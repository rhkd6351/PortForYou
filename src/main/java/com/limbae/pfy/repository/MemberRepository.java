package com.limbae.pfy.repository;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.MemberVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberVO, Long> {

}
