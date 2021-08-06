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
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post")
public class PostVO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column(length = 255, nullable = false)
    String title;

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
    @JoinColumn(name = "board_idx")
    BoardVO board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    UserVO user;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    List<CommentVO> comments;



}
