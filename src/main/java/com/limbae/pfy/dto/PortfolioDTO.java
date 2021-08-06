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
public class PortfolioDTO {
    Long idx;

    String title;

    String content;

    LocalDateTime regDate;

    UserDTO user;

    List<ProjectDTO> project;

    PositionDTO position;

    List<TechDTO> tech;

    EducationDTO education;

    String img;
}
