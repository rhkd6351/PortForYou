package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    Date regDate;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = CascadeType.DETACH)
    @JoinColumn(name = "study_announcement_idx")
    AnnouncementVO announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_idx")
    PositionVO position;

    @ColumnDefault("0")
    Long declined;

}