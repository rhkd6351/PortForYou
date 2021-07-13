package com.limbae.pfy.util;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
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

        EducationDTO educationDTO = EducationDTO.builder()
                .idx(vo.getEducation().getIdx())
                .name(vo.getEducation().getName())
                .build();

        Set<TechDTO> techDTOSet = vo.getTech().stream().map(
                i -> TechDTO.builder()
                        .idx(i.getIdx())
                        .stackIdx(i.getStack().getIdx())
                        .ability(i.getAbility())
                        .content(i.getContent())
                        .build()
        ).collect(Collectors.toSet());


        return PortfolioDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .project(projectDTO)
                .positions(positionDTO)
                .title(vo.getTitle())
                .regDate(vo.getRegDate())
                .education(educationDTO)
                .tech(techDTOSet)
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

    public EducationDTO convertEducationVoToDto(EducationVO vo){
        return EducationDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .build();
    }

    public StudyDTO convertStudyVoToDto(StudyVO vo){

        return StudyDTO.builder()
                .idx(vo.getIdx())
                .user_uid(vo.getUser().getUid())
                .content(vo.getContent())
                .title(vo.getTitle())
                .build();

    }

    public StudyVO convertStudyDtoToVo(StudyDTO vo){

        return StudyVO.builder()
                .content(vo.getContent())
                .title(vo.getTitle())
                .build();

    }

    public AnnouncementDTO convertAnnouncementVoToDto(AnnouncementVO announcementVO){

        AnnouncementDTO build = AnnouncementDTO.builder()
                .idx(announcementVO.getIdx())
                .studyIdx(announcementVO.getStudy().getIdx())
                .title(announcementVO.getTitle())
                .content(announcementVO.getContent())
                .build();

        if(announcementVO.getDemandPositionVOSet() != null){
            build.setDemandPosition(announcementVO.getDemandPositionVOSet().stream().map(
                    i -> DemandPositionDTO.builder()
                                .idx(i.getIdx())
                                .studyAnnouncementIdx(i.getStudyAnnouncementIdx())
                                .demand(i.getDemand())
                                .positionIdx(i.getPositionIdx())
                                .build()
            ).collect(Collectors.toList()));
        }

        return build;
    }

    public AnnouncementVO convertAnnouncementDtoToVo(AnnouncementDTO announcementDTO){

        Set<DemandPositionVO> demandPositionVOSet = announcementDTO.getDemandPosition().stream().map(
                i -> DemandPositionVO.builder()
                        .demand(i.getDemand())
                        .studyAnnouncementIdx(0L) // 일단 0으로 저장
                        .positionIdx(i.getPositionIdx())
                        .build()
        ).collect(Collectors.toSet());

        return AnnouncementVO.builder()
                .content(announcementDTO.getContent())
                .title(announcementDTO.getTitle())
                .demandPositionVOSet(demandPositionVOSet)
                .build();
    }

    public StudyApplicationDTO convertStudyApplicationVoToDto(StudyApplicationVO vo){


        return StudyApplicationDTO.builder()
                .idx(vo.getIdx())
                .announcement(AnnouncementDTO.builder().idx(vo.getAnnouncement().getIdx()).build())
                .portfolio(PortfolioDTO.builder().idx(vo.getPortfolio().getIdx()).build())
                .position(PositionDTO.builder().idx(vo.getPosition().getIdx()).build())
                .regDate(vo.getRegDate())
                .build();
    }

    public UserDTO convertUserVoToDto(UserVO vo){
        return UserDTO.builder()
                .uid(vo.getUid())
                .name(vo.getName())
                .phone(vo.getPhone())
                .site(vo.getSite())
                .build();
    }
}
