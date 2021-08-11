package com.limbae.pfy.service.channel;

import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.dto.Channel.MessageDTO;
import javassist.NotFoundException;

import javax.security.auth.message.AuthException;

public interface MessageServiceInterface {

    public MessageVO update(MessageVO vo);

    public MessageVO update(MessageDTO dto) throws Exception;

    public MessageVO getByIdx(Long idx) throws NotFoundException;

}
