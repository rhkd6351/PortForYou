package com.limbae.pfy.service.channel;

import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.Channel.MessageDTO;
import com.limbae.pfy.repository.channel.MessageRepository;
import com.limbae.pfy.service.user.UserServiceInterface;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageServiceInterfaceImpl implements MessageServiceInterface{

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RoomServiceInterface roomServiceInterface;

    @Autowired
    UserServiceInterface userServiceInterface;

    @Override
    public MessageVO update(MessageVO vo) {
        return messageRepository.save(vo);
    }

    @Override
    public MessageVO update(MessageDTO dto) throws Exception {
        //        UserVO user = userServiceInterface.getByAuth();
        RoomVO room = roomServiceInterface.getByIdx(dto.getRoom().getIdx());
        UserVO user = userServiceInterface.getByUid(dto.getUser().getUid());

        MessageVO message = null;
        if(dto.getIdx() == null){
            message = MessageVO.builder()
                    .message(dto.getMessage())
                    .room(room)
                    .user(user)
                    .type(dto.getType())
                    .build();
        }else{
            message = this.getByIdx(dto.getIdx());
            message.setMessage(dto.getMessage());
            message.setRoom(room);
            message.setUser(user);
        }
        return messageRepository.save(message);
    }

    @Override
    public MessageVO getByIdx(Long idx) throws NotFoundException {
        Optional<MessageVO> message = messageRepository.findById(idx);
        if(message.isEmpty())
            throw new NotFoundException("invalid message idx");
        return message.get();
    }
}
