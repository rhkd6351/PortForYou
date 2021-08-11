package com.limbae.pfy.dto.study;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
