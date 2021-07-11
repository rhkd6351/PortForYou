package com.limbae.pfy.dto;

import com.limbae.pfy.domain.EducationVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDTO {
    int idx;

    String title;

    String content;

    Date regDate;

    Set<ProjectDTO> project;

    Set<PositionDTO> positions;

    Set<TechDTO> tech;

    EducationDTO education;
}
