package com.limbae.pfy.domain;


import lombok.*;

import javax.persistence.*;

@Entity(name = "authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;

    @Column(name = "authority_name", length = 50)
    private String authorityName;

}
