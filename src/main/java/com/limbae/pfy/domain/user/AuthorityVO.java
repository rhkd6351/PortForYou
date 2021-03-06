package com.limbae.pfy.domain.user;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "authority")
public class AuthorityVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @Column(name = "authority_name", length = 50)
    @Enumerated(value = EnumType.STRING)
    Authority authority;

}
