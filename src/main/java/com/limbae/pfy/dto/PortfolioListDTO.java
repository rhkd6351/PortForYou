package com.limbae.pfy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioListDTO {
    Long idx;
    String title;
    String content;
    LocalDateTime reg_date;

    List<PositionDTO> position;
    List<StackDTO> stack ;
    EducationDTO education;
}
