package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tech")
public class TechVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @ManyToOne(targetEntity = PortfolioVO.class,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    String content;

    int ability;

    @ManyToOne(targetEntity = StackVO.class,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "stack_idx")
    StackVO stack;

}
