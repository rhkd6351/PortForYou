package com.limbae.pfy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

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
@Entity
@Table(name = "study_user")
public class MemberVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_uid")
    UserVO user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="study_idx")
    StudyVO study;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_idx")
    PositionVO position;
}









