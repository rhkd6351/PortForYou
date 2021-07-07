package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    int idx;

    @ManyToOne(targetEntity = AnnouncementVO.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "study_announcement_idx")
    AnnouncementVO announcement;

    @ManyToOne(targetEntity = PortfolioVO.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    @Column(name = "reg_datre")
    @CreationTimestamp
    Date regDate;

}
