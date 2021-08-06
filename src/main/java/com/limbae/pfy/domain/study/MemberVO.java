package com.limbae.pfy.domain.study;


import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.user.UserVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_uid")
    UserVO user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_idx")
    StudyVO study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_idx")
    PositionVO position;
}









