package com.limbae.pfy.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "project")
public class ProjectVO {

    @Column(name = "idx")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @ManyToOne(targetEntity = PortfolioVO.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    String title;

    String content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_stack",
            joinColumns = {@JoinColumn(name = "project_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "stack_idx", referencedColumnName = "idx")})
    Set<StackVO> stack;
}
