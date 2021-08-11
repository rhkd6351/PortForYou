package com.limbae.pfy.controller.channel;


import com.limbae.pfy.domain.channel.MessageType;
import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.dto.Channel.MessageDTO;
import com.limbae.pfy.dto.Channel.RoomDTO;
import com.limbae.pfy.service.channel.MessageServiceInterface;
import com.limbae.pfy.service.channel.RoomServiceInterface;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChannelController {

    @Autowired
    RoomServiceInterface roomService;

    @Autowired
    MessageServiceInterface messageService;

    @Autowired
    EntityUtil entityUtil;

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(MessageDTO messageDTO) throws Exception {
        if(MessageType.ENTER.equals(messageDTO.getType()))
            messageDTO.setMessage("Entered");

        MessageVO message = messageService.update(messageDTO);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getRid(), message);
    }

    @GetMapping("/study/{study-idx}/room")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RoomDTO> getRoom(
            @PathVariable(value = "study-idx") Long studyIdx) throws NotFoundException {

        RoomVO room = roomService.getByStudyIdx(studyIdx);

        return ResponseEntity.ok(entityUtil.convertRoomVoToDto(room));
    }




















}
