package com.limbae.pfy.dto;


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









