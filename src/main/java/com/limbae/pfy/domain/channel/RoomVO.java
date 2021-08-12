package com.limbae.pfy.domain.channel;

import com.limbae.pfy.domain.study.StudyVO;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "room")
public class RoomVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_idx")
    StudyVO study;

    String rid;

}
