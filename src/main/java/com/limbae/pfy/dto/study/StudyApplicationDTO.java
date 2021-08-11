package com.limbae.pfy.dto.study;

import com.limbae.pfy.dto.etc.PositionDTO;
import com.limbae.pfy.dto.user.PortfolioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyApplicationDTO {

    Long idx;

    LocalDateTime regDate;

    AnnouncementDTO announcement;

    PortfolioDTO portfolio;

    PositionDTO position;

    Long declined;


}