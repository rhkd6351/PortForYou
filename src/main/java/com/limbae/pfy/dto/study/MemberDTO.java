package com.limbae.pfy.dto.study;


import com.limbae.pfy.dto.etc.PositionDTO;
import com.limbae.pfy.dto.study.StudyDTO;
import com.limbae.pfy.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

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

    LocalDateTime regDate;

}









