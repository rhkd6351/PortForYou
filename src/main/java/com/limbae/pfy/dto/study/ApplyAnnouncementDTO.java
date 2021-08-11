package com.limbae.pfy.dto.study;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
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

    List<Integer> demandPosition;

}
