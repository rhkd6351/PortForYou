package com.limbae.pfy.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "project")
public class ProjectVO {

    @Column(name = "uid")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int uid;

    String title;

    String content;

    int portfolio_uid;

    @ManyToMany
    @JoinTable(
            name = "project_stack",
            joinColumns = {@JoinColumn(name = "project_uid", referencedColumnName = "uid")},
            inverseJoinColumns = {@JoinColumn(name = "stack_name", referencedColumnName = "name")})
    private List<StackVO> stack;
}
