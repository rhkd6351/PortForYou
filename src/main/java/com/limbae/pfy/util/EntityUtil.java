package com.limbae.pfy.util;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.PortfolioDTO;
import com.limbae.pfy.dto.PositionDTO;
import com.limbae.pfy.dto.ProjectDTO;
import com.limbae.pfy.dto.StackDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EntityUtil {



    public PortfolioDTO convertPortfolioVoToDto(PortfolioVO vo){
        Set<PositionDTO> positionDTO = vo.getPosition().stream().map(
                i -> PositionDTO.builder()
                        .idx(i.getIdx())
                        .name(i.getName())
                        .build()
        ).collect(Collectors.toSet());

        Set<ProjectDTO> projectDTO = vo.getProject().stream().map(
                i -> ProjectDTO.builder()
                        .idx(i.getIdx())
                        .title(i.getTitle())
                        .content(i.getContent())
                        .stack(i.getStack().stream().map(
                                t -> StackDTO.builder()
                                        .idx(t.getIdx())
                                        .name(t.getName())
                                        .content(t.getContent())
                                        .build()
                        ).collect(Collectors.toSet()))
                        .build()
        ).collect(Collectors.toSet());


        return PortfolioDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .project(projectDTO)
                .positions(positionDTO)
                .title(vo.getTitle())
                .regDate(vo.getRegDate())
                .build();
    }


    public StackDTO convertStackVoToDto(StackVO vo){

        return StackDTO.builder()
                .name(vo.getName())
                .content(vo.getContent())
                .idx(vo.getIdx())
                .build();
    }

    public PositionDTO convertPositionVoToDto(PositionVO vo){

        return PositionDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .build();
    }
}
