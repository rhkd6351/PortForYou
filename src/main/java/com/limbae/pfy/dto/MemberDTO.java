package com.limbae.pfy.dto;


import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.domain.UserVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    Long idx;

    UserDTO user;

    StudyDTO study;

    PositionDTO position;

    Date regDate;

}









