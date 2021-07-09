package com.limbae.pfy.dto;

import com.limbae.pfy.domain.DemandPositionVO;
import com.limbae.pfy.domain.StudyApplicationVO;
import com.limbae.pfy.domain.StudyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {

    Long idx;

    Long studyIdx;

    String title;

    String content;

    List<DemandPositionDTO> demandPosition;

}
