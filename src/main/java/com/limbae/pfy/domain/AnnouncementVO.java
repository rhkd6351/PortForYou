package com.limbae.pfy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    @Column(length = 200, nullable = false)
    String title;

    @Lob
    String content;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @Column(name = "end_date")
    LocalDateTime endDate;
    
    boolean activated;

    @OneToMany(targetEntity = StudyApplicationVO.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_announcement_idx")
    List<StudyApplicationVO> studyApplications;

    @ManyToOne(targetEntity = StudyVO.class,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    StudyVO study;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_announcement_idx")
    Set<DemandPositionVO> demandPosition;

}
