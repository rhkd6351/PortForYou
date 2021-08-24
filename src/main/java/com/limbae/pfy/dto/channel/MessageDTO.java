package com.limbae.pfy.dto.channel;

import com.limbae.pfy.domain.channel.MessageType;
import com.limbae.pfy.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
