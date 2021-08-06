package com.limbae.pfy.domain.study;

import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.user.PortfolioVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "study_application")
public class StudyApplicationVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @ColumnDefault("0")
    Long declined;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_announcement_idx")
    AnnouncementVO announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_idx")
    PositionVO position;

}