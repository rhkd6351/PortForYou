package com.limbae.pfy.dto.study;

import com.limbae.pfy.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyDTO {
    Long idx;

    UserDTO user;

    String title;

    String content;

    StudyCategoryDTO studyCategory;

    int numberOfMembers;
}