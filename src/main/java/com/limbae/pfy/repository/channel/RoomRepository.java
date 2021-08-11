package com.limbae.pfy.repository.channel;

import com.limbae.pfy.domain.channel.RoomVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomVO, Long> {

    public List<RoomVO> findByRid(String rid);
    

}
