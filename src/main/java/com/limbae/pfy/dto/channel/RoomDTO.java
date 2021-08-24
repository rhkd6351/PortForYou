package com.limbae.pfy.dto.channel;

import com.limbae.pfy.dto.study.StudyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {

    Long idx;

    StudyDTO study;

    String rid;
}
