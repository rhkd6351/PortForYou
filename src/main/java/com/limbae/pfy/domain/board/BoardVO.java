package com.limbae.pfy.domain.board;

import com.limbae.pfy.domain.StudyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board")
public class BoardVO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @Column(length = 100, nullable = false)
    String name;

    @Column(length = 255, nullable = false)
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_idx")
    StudyVO study;

}
