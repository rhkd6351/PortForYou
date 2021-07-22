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


    public StudyCategoryDTO convertStudyCategoryVoToDto(StudyCategoryVO vo){
        return StudyCategoryDTO.builder()
                .idx(vo.getIdx())
                .title(vo.getTitle())
                .content(vo.getContent())
                .build();
    }

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

        StudyCategoryVO studyCategory = vo.getStudyCategory();

        StudyCategoryDTO studyCategoryBuild = StudyCategoryDTO.builder()
                .idx(studyCategory.getIdx())
                .title(studyCategory.getTitle())
                .content(studyCategory.getContent())
                .build();

        return StudyDTO.builder()
                .idx(vo.getIdx())
                .user_uid(vo.getUser().getUid())
                .content(vo.getContent())
                .title(vo.getTitle())
                .studyCategory(studyCategoryBuild)
                .build();

    }

    public StudyVO convertStudyDtoToVo(StudyDTO vo){

//        StudyCategoryVO studyCategoryVO = StudyCategoryVO.builder()
//                .idx(vo.getIdx())
//                .title(vo.getTitle())
//                .content(vo.getContent())
//                .build();

        return StudyVO.builder()
                .content(vo.getContent())
                .title(vo.getTitle())
//                .studyCategory(studyCategoryVO)
                .build();

    }

    public AnnouncementDTO convertAnnouncementVoToDto(AnnouncementVO announcementVO){

        AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .idx(announcementVO.getIdx())
                .studyIdx(announcementVO.getStudy().getIdx())
                .title(announcementVO.getTitle())
                .content(announcementVO.getContent())
                .regDate(announcementVO.getRegDate())
                .endDate(announcementVO.getEndDate())
                .activated(announcementVO.isActivated())
                .build();


        if(announcementVO.getDemandPosition() != null){
            announcementDTO.setDemandPosition(announcementVO.getDemandPosition().stream().map(
                    i -> DemandPositionDTO.builder()
                                .idx(i.getIdx())
                                .studyAnnouncementIdx(i.getStudyAnnouncementIdx())
                                .demand(i.getDemand())
                                .position(PositionDTO.builder()
                                            .idx(i.getPosition().getIdx())
                                            .name(i.getPosition().getName())
                                            .build())
                                .build()
            ).collect(Collectors.toList()));
        }

        return announcementDTO;
    }

    public AnnouncementVO convertAnnouncementDtoToVo(AnnouncementDTO announcementDTO){

        Set<DemandPositionVO> demandPosition = announcementDTO.getDemandPosition().stream().map(
                i -> DemandPositionVO.builder()
                        .demand(i.getDemand())
                        .studyAnnouncementIdx(0L) // 일단 0으로 저장
                        .position(PositionVO.builder()
                                .idx(i.getPosition().getIdx())
                                .name(i.getPosition().getName())
                                .build())
                        .applied(0)
                        .build()
        ).collect(Collectors.toSet());

        return AnnouncementVO.builder()
                .content(announcementDTO.getContent())
                .title(announcementDTO.getTitle())
                .demandPosition(demandPosition)
                .activated(true)
                .build();
    }

    public StudyApplicationDTO convertStudyApplicationVoToDto(StudyApplicationVO vo){


        return StudyApplicationDTO.builder()
                .idx(vo.getIdx())
                .announcement(AnnouncementDTO.builder()
                        .idx(vo.getAnnouncement().getIdx())
                        .title(vo.getAnnouncement().getTitle())
                        .activated(vo.getAnnouncement().isActivated())
                        .build())
                .portfolio(PortfolioDTO.builder()
                        .idx(vo.getPortfolio().getIdx())
                        .title(vo.getPortfolio().getTitle())
                        .build())
                .position(PositionDTO.builder()
                        .idx(vo.getPosition().getIdx())
                        .name(vo.getPosition().getName())
                        .build())
                .regDate(vo.getRegDate())
                .declined(vo.getDeclined())
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
