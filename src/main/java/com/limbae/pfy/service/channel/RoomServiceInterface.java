package com.limbae.pfy.service.channel;

import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.dto.Channel.RoomDTO;
import javassist.NotFoundException;

public interface RoomServiceInterface {

    public RoomVO update(RoomVO vo);

    public RoomVO update(RoomDTO dto) throws NotFoundException;

    public RoomVO getByIdx(Long idx) throws NotFoundException;

    public RoomVO getByStudyIdx(Long idx) throws NotFoundException;

    public RoomVO getByRid(String rid) throws NotFoundException;

}
