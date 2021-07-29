package com.limbae.pfy.dto;


import com.limbae.pfy.domain.StudyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyCategoryDTO {

    Long idx;

    String title;

    String content;

    List<StudyDTO> studyList;
}
