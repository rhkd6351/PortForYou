package com.limbae.pfy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "position")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 100, nullable = false, unique = true)
    String name;
}
