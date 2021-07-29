package com.limbae.pfy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "study_idx")
    StudyVO study;

    @OneToMany(mappedBy = "announcement")
    List<StudyApplicationVO> studyApplications;

    @OneToMany(mappedBy = "announcement")
    List<DemandPositionVO> demandPosition;

}
