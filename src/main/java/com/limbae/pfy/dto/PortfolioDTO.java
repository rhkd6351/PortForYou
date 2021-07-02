package com.limbae.pfy.dto;

import com.limbae.pfy.domain.PositionVO;
import com.limbae.pfy.domain.ProjectVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDTO {
    int idx;

    String title;

    String content;

    int user_uid;

    Date regDate;

    Set<ProjectVO> project;

    Set<PositionVO> positions;
}
