package com.limbae.pfy.controller.etc;


import com.limbae.pfy.domain.etc.EducationVO;
import com.limbae.pfy.domain.etc.PositionVO;
import com.limbae.pfy.domain.etc.StackVO;
import com.limbae.pfy.domain.study.StudyCategoryVO;
import com.limbae.pfy.dto.etc.EducationDTO;
import com.limbae.pfy.dto.etc.PositionDTO;
import com.limbae.pfy.dto.etc.StackDTO;
import com.limbae.pfy.dto.study.StudyCategoryDTO;
import com.limbae.pfy.service.etc.EducationService;
import com.limbae.pfy.service.etc.PositionService;
import com.limbae.pfy.service.etc.StackService;
import com.limbae.pfy.service.etc.StudyCategoryService;
import com.limbae.pfy.util.EntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = {"/api/resource"})
@Slf4j
public class ResourceController {

    EntityUtil entityUtil;
    StackService stackService;
    PositionService positionService;
    EducationService educationService;
    StudyCategoryService studyCategoryService;

    public ResourceController(EntityUtil entityUtil, StackService stackService, PositionService positionService, EducationService educationService, StudyCategoryService studyCategoryService) {
        this.entityUtil = entityUtil;
        this.stackService = stackService;
        this.positionService = positionService;
        this.educationService = educationService;
        this.studyCategoryService = studyCategoryService;
    }

    @GetMapping("/stacks")
    public ResponseEntity<List<StackDTO>> getStackList(
            @RequestParam(value = "query", required = false) String query) {

        List<StackVO> stacks = null;

        if(query != null){
            stacks = stackService.getByQuery(query);
        }else{
            stacks = stackService.getAll();
        }

        List<StackDTO> stackDTOs = stacks.stream().map(
                entityUtil::convertStackVoToDto
        ).collect(Collectors.toList());

        return ResponseEntity.ok(stackDTOs);
    }

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getPositionList() {
        List<PositionVO> positions = positionService.getAll();

        List<PositionDTO> positionList = positions.stream().map(
                entityUtil::convertPositionVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(positionList);
    }

    @GetMapping("/educations")
    public ResponseEntity<List<EducationDTO>> getEducationList() {
        List<EducationVO> educations = educationService.getAll();

        List<EducationDTO> educationList = educations.stream().map(
                entityUtil::convertEducationVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(educationList);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<StudyCategoryDTO>> getCategoryList() {
        List<StudyCategoryVO> categoryList = studyCategoryService.getAll();

        List<StudyCategoryDTO> studyCategoryDTOList = categoryList.stream().map(
                i -> entityUtil.convertStudyCategoryVoToDto(i)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(studyCategoryDTOList);
    }
}
