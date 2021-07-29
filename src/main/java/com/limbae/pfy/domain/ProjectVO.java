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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_stack",
            joinColumns = {@JoinColumn(name = "project_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "stack_idx", referencedColumnName = "idx")})
    List<StackVO> stack;
}
