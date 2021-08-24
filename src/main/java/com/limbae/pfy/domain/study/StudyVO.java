package com.limbae.pfy.domain.study;


import com.limbae.pfy.domain.board.CalendarVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.domain.user.UserVO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @OneToOne(mappedBy = "study")
    RoomVO room;

    @OneToMany(mappedBy = "study")
    List<CalendarVO> calendars;

    public void addMember(MemberVO member){
        this.members.add(member);
        member.setStudy(this);
    }


}
