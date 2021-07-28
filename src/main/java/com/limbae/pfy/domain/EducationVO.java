package com.limbae.pfy.domain;

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
public class EducationVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 45)
    String name;
}
