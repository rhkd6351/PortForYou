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

    Long size;

    @Column(name = "upload_path")
    String uploadPath;

}
