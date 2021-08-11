package com.limbae.pfy.domain.channel;

import com.limbae.pfy.domain.study.StudyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "room")
public class RoomVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @OneToOne
    @JoinColumn(name="study_idx")
    StudyVO study;

    String rid;

}
