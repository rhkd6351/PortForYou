package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @Builder.Default
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true) //orphanRemoval = 고아객체 제거
    List<ProjectVO> project = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TechVO> tech = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "portfolio", orphanRemoval = true) // TODO 필요한지 검증하고 삭제할것
    List<StudyApplicationVO> studyApplications = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "position_idx")
    PositionVO position;

    public void addProject(ProjectVO project){
        this.project.add(project);
        project.setPortfolio(this);
    }

    public void addTech(TechVO tech){
        this.tech.add(tech);
        tech.setPortfolio(this);
    }

    public void setProject(List<ProjectVO> projects){
        this.project.clear();
        for (ProjectVO project : projects)
            this.addProject(project);

    }

    public void setTech(Set<TechVO> techs){
        this.tech.clear();
        for (TechVO tech : techs)
            this.addTech(tech);
    }
}