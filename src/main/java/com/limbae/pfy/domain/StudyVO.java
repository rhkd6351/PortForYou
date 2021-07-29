package com.limbae.pfy.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "study")
public class StudyVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 200, nullable = false)
    String title;

    @Lob
    String content;

    @CreationTimestamp
    @Column(name = "reg_date")
    LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    UserVO user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name="study_user",
            joinColumns = {@JoinColumn(name = "study_idx", referencedColumnName = "idx")},
            inverseJoinColumns = {@JoinColumn(name = "user_uid", referencedColumnName = "uid")}
    )
    List<UserVO> members;

    @ManyToOne
    @JoinColumn(name="study_category_idx")
    StudyCategoryVO studyCategory;

    @OneToMany(mappedBy = "study")
    List<MemberVO> membersInfo;

    @OneToMany(mappedBy = "study")
    List<AnnouncementVO> announcements;
}
