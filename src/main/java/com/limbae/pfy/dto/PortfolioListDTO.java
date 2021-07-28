package com.limbae.pfy.dto;

import com.limbae.pfy.domain.PositionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    List<StackDTO> stack;
    EducationDTO education;
}
