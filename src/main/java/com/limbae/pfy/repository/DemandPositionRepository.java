package com.limbae.pfy.repository;

import com.limbae.pfy.domain.AnnouncementVO;
import com.limbae.pfy.domain.DemandPositionVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandPositionRepository extends JpaRepository<DemandPositionVO, Long> {

}
