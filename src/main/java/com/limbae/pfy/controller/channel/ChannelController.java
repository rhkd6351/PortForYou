package com.limbae.pfy.controller.channel;


import com.limbae.pfy.domain.channel.MessageType;
import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.domain.study.AnnouncementVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.Channel.MessageDTO;
import com.limbae.pfy.dto.Channel.RoomDTO;
import com.limbae.pfy.service.channel.MessageServiceInterface;
import com.limbae.pfy.service.channel.RoomServiceInterface;
import com.limbae.pfy.service.user.UserServiceInterface;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChannelController {

    @Autowired
    RoomServiceInterface roomService;

    @Autowired
    MessageServiceInterface messageService;

    @Autowired
    EntityUtil entityUtil;

    @Autowired
    UserServiceInterface userService;

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(MessageDTO messageDTO) throws Exception {
        System.out.println("messageDTO = " + messageDTO);

        UserVO user = userService.getByUid(messageDTO.getUser().getUid());

        if(MessageType.ENTER.equals(messageDTO.getType()))
            messageDTO.setMessage(user.getUsername() + " Entered");

        MessageVO message = messageService.update(messageDTO);

        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoom().getRid(),
                entityUtil.convertMessageVoToDto(message));
    }

    @GetMapping("/study/{study-idx}/room")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RoomDTO> getRoom(
            @PathVariable(value = "study-idx") Long studyIdx) throws NotFoundException {

        RoomVO room = roomService.getByStudyIdx(studyIdx);

        return ResponseEntity.ok(entityUtil.convertRoomVoToDto(room));
    }

    @GetMapping("/study/{study-idx}/room/messages")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<MessageDTO>> getMessage(
            @PathVariable(value = "study-idx") Long studyIdx,
            @RequestParam(value = "last-idx", required = false) Long lastIdx) throws Exception {
        RoomVO room = roomService.getByStudyIdx(studyIdx);
        PageRequest pageRequest = PageRequest.of(0, 30);
        List<MessageVO> messages = new ArrayList<>();

        if(lastIdx == null)
            messages.addAll(messageService.get30Entity(room, pageRequest));
        else{
            messages.addAll(messageService.get30Entity(room, lastIdx, pageRequest));
        }

        return ResponseEntity.ok(messages.stream().map(entityUtil::convertMessageVoToDto).collect(Collectors.toList()));
    }






















}
