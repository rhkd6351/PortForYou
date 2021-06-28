package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
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
    int userId;

    @Column(name = "username")
    String username;

    String password;

    String phone;

    String site;

    @Column(name = "reg_date")
    @CreationTimestamp
    Date regDate;

    @Column(name = "del_date")
    String delDate;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "uid", referencedColumnName = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<AuthorityVO> authorities;


}









