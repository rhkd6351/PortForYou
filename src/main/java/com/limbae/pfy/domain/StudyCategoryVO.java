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
@Entity
@Table(name = "study_category")
public class StudyCategoryVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 100, nullable = false, unique = true)
    String title;

    @Lob
    String content;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "study_category_idx")
    List<StudyVO> studyList;

}
