package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "portfolio")
public class PortfolioVO {

    //Multiple bag 문제가 발생 (2개 이상의 OneToMany or ManyToMany를 담는 List 존재시 에러발생)
    //두개(tech, position)를 set으로 변경

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

    @ManyToOne
    @JoinColumn(name = "user_uid")
    UserVO user;

    @ManyToOne
    @JoinColumn(name = "education_idx")
    EducationVO education;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "portfolio_position",
            joinColumns = {@JoinColumn(name = "portfolio_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "position_idx", referencedColumnName = "idx")})
    Set<PositionVO> position;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "portfolio")
    List<ProjectVO> project;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "portfolio")
    Set<TechVO> tech;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "portfolio")
    List<StudyApplicationVO> studyApplications;
}