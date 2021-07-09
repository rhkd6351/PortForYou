package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "study_announcement")
public class AnnouncementVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    String title;

    String content;


    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_announcement_idx")
    Set<StudyApplicationVO> studyApplications;

    @ManyToOne(targetEntity = StudyVO.class,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    StudyVO study;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "position_idx")
    Set<DemandPositionVO> demandPositionVOSet;

}
