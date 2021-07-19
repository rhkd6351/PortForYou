package com.limbae.pfy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandPositionDTO {

    Long idx;

    Long positionIdx;

    Long studyAnnouncementIdx;

    int demand;





}