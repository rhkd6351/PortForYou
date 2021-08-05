package com.limbae.pfy.domain.board;

import com.limbae.pfy.domain.StudyVO;
import com.limbae.pfy.dto.StudyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    Long idx;

    String name;

    String content;

    Long studyIdx;

}
