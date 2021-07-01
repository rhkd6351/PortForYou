package com.limbae.pfy.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "stack")
public class StackVO {

    @Id
    int idx;

    String name;

    String content;

}
