package com.limbae.pfy.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    Long idx;

    String title;

    String content;

    LocalDateTime regDate;

    LocalDateTime delDate;

    LocalDateTime upDate;

    Long boardIdx;

    String username;
}
