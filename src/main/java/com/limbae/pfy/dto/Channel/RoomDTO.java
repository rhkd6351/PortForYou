package com.limbae.pfy.dto.Channel;

import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.dto.study.StudyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {

    Long idx;

    StudyDTO study;

    String rid;
}
