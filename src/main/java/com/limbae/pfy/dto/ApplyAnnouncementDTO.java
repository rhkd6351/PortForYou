package com.limbae.pfy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyAnnouncementDTO {

    int idx;

    int studyIdx;

    String title;

    String content;

    Set<Integer> demandPosition;

}
