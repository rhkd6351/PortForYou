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

    @OneToMany(mappedBy = "portfolio")
    List<ProjectVO> project;

    @OneToMany(mappedBy = "portfolio")
    Set<TechVO> tech;

    @OneToMany(mappedBy = "portfolio")
    List<StudyApplicationVO> studyApplications;


    //ManyToMany 비효율성 -> 하지만 연결테이블에 아무런 데이터가 없으므로 추후 연결테이블에 데이터 추가시 수정할것
    @ManyToMany
    @JoinTable(
            name = "portfolio_position",
            joinColumns = {@JoinColumn(name = "portfolio_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "position_idx", referencedColumnName = "idx")})
    Set<PositionVO> position;
}