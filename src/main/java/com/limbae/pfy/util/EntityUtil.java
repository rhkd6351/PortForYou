package com.limbae.pfy.util;

import com.limbae.pfy.domain.*;
import com.limbae.pfy.domain.board.BoardDTO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        PositionDTO positionDTO = convertPositionVoToDto(vo.getPosition());

        List<ProjectDTO> projectDTO = vo.getProject().stream().map(
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
                        ).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());

        EducationDTO educationDTO = EducationDTO.builder()
                .idx(vo.getEducation().getIdx())
                .name(vo.getEducation().getName())
                .build();

        List<TechDTO> techDTOSet = vo.getTech().stream().map(
                i -> TechDTO.builder()
                        .idx(i.getIdx())
                        .stackIdx(i.getStack().getIdx())
                        .ability(i.getAbility())
                        .content(i.getContent())
                        .stackName(i.getStack().getName())
                        .build()
        ).collect(Collectors.toList());


        return PortfolioDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .project(projectDTO)
                .position(positionDTO)
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
                .user(this.convertUserVoToDto(vo.getUser()))
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

        StudyDTO studyDTO = StudyDTO.builder()
                .title(announcementVO.getStudy().getTitle())
                .user(this.convertUserVoToDto(announcementVO.getStudy().getUser()))
                .content(announcementVO.getStudy().getContent())
                .studyCategory(StudyCategoryDTO.builder().title(announcementVO.getStudy().getStudyCategory().getTitle()).build())
                .build();

        AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .idx(announcementVO.getIdx())
                .study(studyDTO)
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

        List<DemandPositionVO> demandPosition = announcementDTO.getDemandPosition().stream().map(
                i -> DemandPositionVO.builder()
                        .demand(i.getDemand())
//                        .announcement(null) // 일단 0으로 저장
                        .position(PositionVO.builder()
                                .idx(i.getPosition().getIdx())
                                .name(i.getPosition().getName())
                                .build())
                        .applied(0)
                        .build()
        ).collect(Collectors.toList());

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
                        .tech(vo.getPortfolio().getTech().stream().map(
                                i -> TechDTO.builder().stackIdx(i.getStack().getIdx()).build()
                        ).collect(Collectors.toList()))
                        .user(this.convertUserVoToDto(vo.getPortfolio().getUser()))
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
                .username(vo.getUsername())
                .uid(vo.getUid())
                .name(vo.getName())
                .phone(vo.getPhone())
                .site(vo.getSite())
                .build();
    }

    public BoardDTO convertBoardVoToDto(BoardVO vo){
        return BoardDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .content(vo.getContent())
                .studyIdx(vo.getStudy().getIdx())
                .build();
    }

    public BoardVO convertBoardDtoToVo(BoardDTO dto){
        return BoardVO.builder()
                .idx(dto.getIdx() != null ? dto.getIdx() : null)
                .name(dto.getName())
                .content(dto.getContent())
                .study(null)
                .build();
    }

}










