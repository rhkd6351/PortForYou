package com.limbae.pfy.domain.study;


import com.limbae.pfy.domain.user.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    UserVO user;

    @ManyToOne
    @JoinColumn(name="study_category_idx")
    StudyCategoryVO studyCategory;

    @Builder.Default
    @OneToMany(mappedBy = "study")
    List<MemberVO> members = new ArrayList<>();
}
