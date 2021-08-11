package com.limbae.pfy.repository.channel;

import com.limbae.pfy.domain.channel.MessageVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageVO, Long> {



}
