package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "portfolio")
public class PortfolioVO {

    @Column(name = "idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    String title;

    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    UserVO user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_idx")
    private Set<ProjectVO> project;

    @Column(name = "reg_date")
    @CreationTimestamp
    Date regDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

}