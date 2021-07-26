package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "study")
public class StudyVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

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

    @OneToMany
    @JoinColumn(name = "study_idx")
    Set<MemberVO> membersInfo;

    @ManyToOne(targetEntity = StudyCategoryVO.class,
                fetch = FetchType.EAGER)
    @JoinColumn(name="study_category_idx")
    StudyCategoryVO studyCategory;

    @OneToMany(targetEntity = AnnouncementVO.class,
               cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    List<AnnouncementVO> announcements;


}
