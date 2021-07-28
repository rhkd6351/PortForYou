package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uid;

    @Column(length = 50, unique = true, nullable = false)
    String username;

    @Column(length = 200, nullable = false)
    String password;

    @Column(length = 45)
    String phone;

    @Column(length = 255)
    String site;

    @Column(length = 45)
    String name;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @Column(name = "del_date")
    LocalDateTime delDate;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated; // 0 or 1

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









