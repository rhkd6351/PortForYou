package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechVO {

    @Id
    int idx;

    @Column(name = "portfolio_idx")
    int portfolioIdx;

    String content;

    int ability;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tech_stack",
            joinColumns = {@JoinColumn(name = "tech_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "stack_idx", referencedColumnName = "idx")})
    private List<StackVO> stack;

}
