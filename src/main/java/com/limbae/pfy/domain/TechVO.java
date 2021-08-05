package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tech")
public class TechVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Lob
    String content;

    int ability;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    PortfolioVO portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stack_idx")
    StackVO stack;

}
