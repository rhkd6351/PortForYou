package com.limbae.pfy.dto.etc;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechDTO {

    Long idx;

    String content;

    int ability;

    Long stackIdx;

    String stackName;

}
