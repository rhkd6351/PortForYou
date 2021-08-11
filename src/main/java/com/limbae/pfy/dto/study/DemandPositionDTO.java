package com.limbae.pfy.dto.study;

import com.limbae.pfy.dto.etc.PositionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandPositionDTO {

    Long idx;

    PositionDTO position;

    AnnouncementDTO announcement;

    int demand;

    int applied;





}
