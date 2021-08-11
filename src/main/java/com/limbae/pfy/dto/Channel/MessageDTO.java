package com.limbae.pfy.dto.Channel;

import com.limbae.pfy.domain.channel.MessageType;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    Long idx;

    RoomDTO room;

    UserDTO user;

    String message;

    MessageType type;

    LocalDateTime sendDate;

}
