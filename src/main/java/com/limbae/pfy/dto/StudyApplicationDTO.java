package com.limbae.pfy.dto;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.PositionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyApplicationDTO {

    Long idx;

    Date regDate;

    AnnouncementDTO announcement;

    PortfolioDTO portfolio;

    PositionDTO position;

    Long declined;

}