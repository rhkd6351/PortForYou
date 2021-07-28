package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
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

    @ManyToOne
    @JoinColumn(name = "position_idx", nullable = false)
    PositionVO position;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "study_announcement_idx", nullable = false)
    AnnouncementVO announcement;




}
