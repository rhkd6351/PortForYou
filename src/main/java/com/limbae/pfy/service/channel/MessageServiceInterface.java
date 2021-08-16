package com.limbae.pfy.service.channel;

import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.dto.Channel.MessageDTO;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;

import javax.security.auth.message.AuthException;

import java.util.List;

public interface MessageServiceInterface {

    public MessageVO update(MessageVO vo);

    public MessageVO update(MessageDTO dto) throws Exception;

    public MessageVO getByIdx(Long idx) throws NotFoundException;

    public List<MessageVO> get30Entity(RoomVO room, Pageable pageable) throws Exception;

    public List<MessageVO> get30Entity(RoomVO room, long idx, Pageable pageable) throws Exception;

}
