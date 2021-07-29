package com.limbae.pfy.dto;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.ProjectVO;
import com.limbae.pfy.domain.StackVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    int idx;

    String title;

    String content;

    List<StackDTO> stack ;

    String site;
}
