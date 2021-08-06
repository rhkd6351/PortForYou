package com.limbae.pfy.service.study;
import com.limbae.pfy.domain.study.MemberVO;
import com.limbae.pfy.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

   MemberRepository memberRepository;

   public MemberVO save(MemberVO vo){
       return memberRepository.save(vo);
   }


}
