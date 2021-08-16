package com.limbae.pfy.repository.channel;

import com.limbae.pfy.domain.channel.MessageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface MessageRepository extends JpaRepository<MessageVO, Long> {

    @Query("select m from MessageVO m where m.room.rid = :rid order by m.idx desc")
    public List<MessageVO> findByRid(String rid, Pageable pageable);

    @Query("select m from MessageVO m where m.room.rid = :rid AND m.idx < :idx order by m.idx desc")
    public List<MessageVO> findByRid(String rid, Long idx, Pageable pageable);

}
