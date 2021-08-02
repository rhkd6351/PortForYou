package com.limbae.pfy.domain;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class ProjectVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @Column(length = 200, nullable = false)
    String title;

    @Lob
    @Column(nullable = false)
    String content;

    @Column(length = 255)
    String site;

    @ManyToOne
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    //ManyToMany 비효율성 -> 하지만 연결테이블에 아무런 데이터가 없으므로 추후 연결테이블에 데이터 추가시 수정할것
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "project_stack",
            joinColumns = {@JoinColumn(name = "project_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "stack_idx", referencedColumnName = "idx")})
    List<StackVO> stack = new ArrayList<>();
}
