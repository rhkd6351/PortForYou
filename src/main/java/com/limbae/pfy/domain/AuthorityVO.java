package com.limbae.pfy.domain;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityVO {

    @Id
    private int idx;

    @Column(name = "authority_name", length = 50)
    private String authorityName;

}
