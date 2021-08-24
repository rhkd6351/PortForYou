package com.limbae.pfy.dto.board;

import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.study.StudyDTO;
import com.limbae.pfy.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDTO {

    Long idx;

    StudyDTO study;

    UserDTO user;

    String title;

    String content;

    LocalDateTime fromDate;

    LocalDateTime toDate;

    LocalDateTime regDate;

    LocalDateTime modDate;

}
