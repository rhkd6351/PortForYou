package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
public class UserVO {

    @Column(name = "uid")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uid;

    @Column(name = "username")
    String username;

    String password;

    String phone;

    String site;

    String name;

    @Column(name = "reg_date")
    @CreationTimestamp
    Date regDate;

    @Column(name = "del_date")
    String delDate;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_uid", referencedColumnName = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "authority_idx", referencedColumnName = "idx")})
    private Set<AuthorityVO> authorities;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private List<PortfolioVO> portfolio;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name="study_user",
            joinColumns = {@JoinColumn(name = "user_uid", referencedColumnName = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "study_idx", referencedColumnName = "idx")}
    )
    Set<StudyVO> study;


}









