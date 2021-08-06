package com.limbae.pfy.domain.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    StudyVO study;

    @Builder.Default
    @OneToMany( mappedBy = "announcement", orphanRemoval = true)
    List<DemandPositionVO> demandPosition = new ArrayList<>();

}
