package com.limbae.pfy.domain.study;

import com.limbae.pfy.domain.etc.PositionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false)
    int demand;

    @ColumnDefault(value = "0")
    int applied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_idx", nullable = false)
    PositionVO position;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "study_announcement_idx", nullable = false)
    AnnouncementVO announcement;




}
