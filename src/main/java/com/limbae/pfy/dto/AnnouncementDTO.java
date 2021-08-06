package com.limbae.pfy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {

    Long idx;

    StudyDTO study;

    String title;

    String content;

    List<DemandPositionDTO> demandPosition ;

    LocalDateTime regDate;

    LocalDateTime endDate;

    boolean activated;

}
