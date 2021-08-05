package com.limbae.pfy.dto.board;

import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
