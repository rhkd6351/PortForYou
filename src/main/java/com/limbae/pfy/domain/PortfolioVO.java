package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PortfolioVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 100, nullable = false)
    String title;

    @Lob
    String content;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_uid")
    UserVO user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    private Set<ProjectVO> project;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "portfolio_position",
            joinColumns = {@JoinColumn(name = "portfolio_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "position_idx", referencedColumnName = "idx")})
    private Set<PositionVO> position;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    Set<TechVO> tech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_idx")
    EducationVO education;

    @OneToMany(cascade = CascadeType.ALL,
            targetEntity = StudyApplicationVO.class,
                fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    List<StudyApplicationVO> studyApplications;
}