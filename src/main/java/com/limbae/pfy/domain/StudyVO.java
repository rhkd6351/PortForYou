package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "study")
public class StudyVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @ManyToOne(targetEntity = UserVO.class,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_uid")
    UserVO user;

    String title;

    String content;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name="study_user",
            joinColumns = {@JoinColumn(name = "study_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "user_uid", referencedColumnName = "uid")}
    )
    Set<UserVO> members;


}
