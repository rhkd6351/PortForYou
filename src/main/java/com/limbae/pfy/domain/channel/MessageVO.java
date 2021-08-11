package com.limbae.pfy.domain.channel;

import com.limbae.pfy.domain.user.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @ManyToOne
    @JoinColumn(name = "room_idx")
    RoomVO room;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    UserVO user;

    String message;

    MessageType type;

    @Column(name = "send_date")
    @CreationTimestamp
    LocalDateTime sendDate;


}
