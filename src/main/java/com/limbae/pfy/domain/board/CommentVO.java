package com.limbae.pfy.domain.board;

import com.limbae.pfy.domain.user.UserVO;
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
@Entity
@Table(name = "comment")
public class CommentVO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Lob
    String content;

    @Column(name = "reg_date")
    @CreationTimestamp
    LocalDateTime regDate;

    @Column(name = "del_date")
    LocalDateTime delDate;

    @Column(name = "up_date")
    @UpdateTimestamp
    LocalDateTime upDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    PostVO post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    UserVO user;

}
