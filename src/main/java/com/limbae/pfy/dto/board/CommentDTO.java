package com.limbae.pfy.dto.board;

import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.dto.user.UserDTO;
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
public class CommentDTO {

    Long idx;

    String content;

    LocalDateTime regDate;

    LocalDateTime delDate;

    LocalDateTime upDate;

    Long postId;

    UserDTO user;
}
