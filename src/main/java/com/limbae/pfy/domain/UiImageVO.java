package com.limbae.pfy.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "ui_image")
public class UiImageVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    String name;

    @Column(name = "original_name")
    String originalName;

    @Column(name = "save_name")
    String saveName;

    int size;

    @Column(name = "upload_path")
    String uploadPath;

}
