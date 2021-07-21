package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demand_position")
public class DemandPositionVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_idx")
    PositionVO position;

    @Column(name = "study_announcement_idx")
    Long studyAnnouncementIdx;

    int demand;





}
