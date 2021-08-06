package com.limbae.pfy.service.study;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.dto.StudyDTO;
import javassist.NotFoundException;
import javax.security.auth.message.AuthException;
import java.util.List;


public interface StudyServiceInterface {

    public List<StudyVO> getByUid(Long uid) throws Exception;

    public List<StudyVO> getAppliedStudiesByUid(Long uid) throws Exception;

    public StudyVO getByIdx(Long idx) throws NotFoundException;

    public StudyVO update(StudyDTO dto) throws NotFoundException, AuthException;

    public void delete(StudyVO vo);

    public void memberCheck(Long idx) throws AuthException, NotFoundException;

    public void managerCheck(Long idx) throws NotFoundException, AuthException;

}
