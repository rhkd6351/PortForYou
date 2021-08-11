package com.limbae.pfy.service.channel;

import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.domain.study.StudyVO;
import com.limbae.pfy.dto.Channel.RoomDTO;
import com.limbae.pfy.repository.channel.RoomRepository;
import com.limbae.pfy.service.study.StudyServiceInterface;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomServiceInterfaceImpl implements RoomServiceInterface{

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    StudyServiceInterface studyService;

    @Override
    public RoomVO update(RoomVO vo) {
        return roomRepository.save(vo);
    }

    @Override
    public RoomVO update(RoomDTO dto) throws NotFoundException {
        StudyVO study = studyService.getByIdx(dto.getStudy().getIdx());

        RoomVO room = null;
        if(dto.getIdx() == null){
            room = RoomVO.builder()
                    .study(study)
                    .rid(UUID.randomUUID().toString())
                    .build();
        }else{
            room = this.getByIdx(dto.getIdx());
            room.setStudy(study);
        }

        return this.update(room);

    }

    @Override
    public RoomVO getByIdx(Long idx) throws NotFoundException {
        Optional<RoomVO> room = roomRepository.findById(idx);
        if(room.isEmpty())
            throw new NotFoundException("invalid room idx");
        return room.get();
    }

    @Override
    public RoomVO getByStudyIdx(Long idx) throws NotFoundException {
        StudyVO study = studyService.getByIdx(idx);
        return study.getRoom();
    }

    @Override
    public RoomVO getByRid(String rid) throws NotFoundException {
        List<RoomVO> roomList = roomRepository.findByRid(rid);
        if(roomList.size() == 0)
            throw new NotFoundException("invalid rid");
        return roomList.stream().findAny().get();
    }
}
