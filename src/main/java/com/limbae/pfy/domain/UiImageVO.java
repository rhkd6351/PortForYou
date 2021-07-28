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
@Table(name = "ui_image")
public class UiImageVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idx;

    @Column(length = 255, nullable = false)
    String name;

    @Column(name = "original_name", length = 255, nullable = false)
    String originalName;

    @Column(name = "save_name", length = 255, nullable = false)
    String saveName;

    Long size;

    @Column(name = "upload_path", length = 255, nullable = false)
    String uploadPath;

}
