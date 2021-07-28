package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "study_announcement_idx")
    AnnouncementVO announcement;

    @ManyToOne
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    @ManyToOne
    @JoinColumn(name = "position_idx")
    PositionVO position;

}