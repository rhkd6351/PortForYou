package com.limbae.pfy.dto;

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