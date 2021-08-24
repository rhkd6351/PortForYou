package com.limbae.pfy.util;

import com.limbae.pfy.domain.board.CalendarVO;
import com.limbae.pfy.domain.board.CommentVO;
import com.limbae.pfy.domain.board.PostVO;
import com.limbae.pfy.domain.channel.MessageVO;
import com.limbae.pfy.domain.channel.RoomVO;
import com.limbae.pfy.domain.etc.*;
import com.limbae.pfy.domain.study.*;
import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.board.CalendarDTO;
import com.limbae.pfy.dto.channel.MessageDTO;
import com.limbae.pfy.dto.channel.RoomDTO;
import com.limbae.pfy.dto.board.BoardDTO;
import com.limbae.pfy.domain.board.BoardVO;
import com.limbae.pfy.dto.board.CommentDTO;
import com.limbae.pfy.dto.board.PostDTO;
import com.limbae.pfy.dto.etc.*;
import com.limbae.pfy.dto.study.*;
import com.limbae.pfy.dto.user.PortfolioDTO;
import com.limbae.pfy.dto.user.UserDTO;
import com.limbae.pfy.service.etc.ImageService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EntityUtil {

    private final String serverUri;
    ImageService imageService;

    public EntityUtil(@Value("${server.uri}")String serverUri,
                      ImageService imageService) {
        this.serverUri = serverUri;
        this.imageService = imageService;
    }

    public RoomDTO convertRoomVoToDto(RoomVO vo){
        StudyDTO studyDTO = StudyDTO.builder().idx(vo.getStudy().getIdx()).build();
        return RoomDTO.builder()
                .idx(vo.getIdx())
                .study(studyDTO)
                .rid(vo.getRid())
                .build();
    }
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
                this::convertProjectVoToDto
        ).collect(Collectors.toList());

        EducationDTO educationDTO = this.convertEducationVoToDto(vo.getEducation());

        List<TechDTO> techDTOSet = vo.getTech().stream().map(
                i -> TechDTO.builder()
                        .idx(i.getIdx())
                        .stackIdx(i.getStack().getIdx())
                        .ability(i.getAbility())
                        .content(i.getContent())
                        .stackName(i.getStack().getName())
                        .build()
        ).collect(Collectors.toList());

        UserDTO userDTO = this.convertUserVoToDto(vo.getUser());


        return PortfolioDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .project(projectDTO)
                .position(positionDTO)
                .title(vo.getTitle())
                .regDate(vo.getRegDate())
                .education(educationDTO)
                .tech(techDTOSet)
                .user(userDTO)
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

    public ProjectDTO convertProjectVoToDto(ProjectVO vo){
        return ProjectDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .site(vo.getSite())
                .title(vo.getTitle())
                .stack(vo.getStack().stream().map(
                        this::convertStackVoToDto
                ).collect(Collectors.toList()))
                .build();
    }

    public MessageDTO convertMessageVoToDto(MessageVO vo){
        return MessageDTO.builder()
                .idx(vo.getIdx())
                .room(this.convertRoomVoToDto(vo.getRoom()))
                .user(this.convertUserVoToDto(vo.getUser()))
                .message(vo.getMessage())
                .type(vo.getType())
                .sendDate(vo.getSendDate())
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
                .portfolio(this.convertPortfolioVoToDto(vo.getPortfolio()))
                .position(PositionDTO.builder()
                        .idx(vo.getPosition().getIdx())
                        .name(vo.getPosition().getName())
                        .build())
                .regDate(vo.getRegDate())
                .declined(vo.getDeclined())
                .build();
    }

    public UserDTO convertUserVoToDto(UserVO vo){

        UserDTO userDTO = UserDTO.builder()
                .username(vo.getUsername())
                .uid(vo.getUid())
                .name(vo.getName())
                .phone(vo.getPhone())
                .site(vo.getSite())
                .build();

        try{
            ImageVO image = imageService.getByName(vo.getUid() + "_profile_img");
            String uri = serverUri + "/api/img/default/" + image.getName();
            userDTO.setImg(uri);
        }catch (NotFoundException e){
            userDTO.setImg("not registered");
        }

        return userDTO;
    }

    public BoardDTO convertBoardVoToDto(BoardVO vo){
        return BoardDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .content(vo.getContent())
                .studyIdx(vo.getStudy().getIdx())
                .build();
    }

    public PostDTO convertPostVoToDto(PostVO vo){
        return PostDTO.builder()
                .idx(vo.getIdx())
                .title(vo.getTitle())
                .content(vo.getContent())
                .regDate(vo.getRegDate())
                .upDate(vo.getUpDate())
                .delDate(vo.getDelDate())
                .boardIdx(vo.getBoard().getIdx())
                .user(this.convertUserVoToDto(vo.getUser()))
                .build();
    }

    public CommentDTO convertCommentVoToDto(CommentVO vo){
        return CommentDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .regDate(vo.getRegDate())
                .delDate(vo.getDelDate())
                .upDate(vo.getUpDate())
                .postId(vo.getPost().getIdx())
                .user(this.convertUserVoToDto(vo.getUser()))
                .build();
    }

    public CalendarDTO convertCalendarVoToDto(CalendarVO vo){
        return CalendarDTO.builder()
                .user(this.convertUserVoToDto(vo.getUser()))
                .study(null)
                .fromDate(vo.getFromDate())
                .toDate(vo.getToDate())
                .modDate(vo.getModDate())
                .regDate(vo.getRegDate())
                .title(vo.getTitle())
                .content(vo.getContent())
                .idx(vo.getIdx())
                .build();
    }
}










