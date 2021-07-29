package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
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
    boolean activated; // 0 or 1

    //ManyToMany 비효율성 -> 하지만 연결테이블에 아무런 데이터가 없으므로 추후 연결테이블에 데이터 추가시 수정할것
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_uid", referencedColumnName = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "authority_idx", referencedColumnName = "idx")})
    List<AuthorityVO> authorities;
}









