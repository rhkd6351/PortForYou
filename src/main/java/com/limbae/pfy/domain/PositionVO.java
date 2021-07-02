package com.limbae.pfy.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "position")
public class PositionVO {
    @Id
    int idx;

    String name;
}
