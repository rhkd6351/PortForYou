package com.limbae.pfy.controller;


import com.limbae.pfy.domain.*;
import com.limbae.pfy.dto.*;
import com.limbae.pfy.repository.EducationRepository;
import com.limbae.pfy.service.*;
import com.limbae.pfy.util.EntityUtil;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<List<StackDTO>> getStackList() {
        List<StackVO> stacks = stackService.getStackList();

        List<StackDTO> stackDTOs = stacks.stream().map(
                entityUtil::convertStackVoToDto
        ).collect(Collectors.toList());

        return ResponseEntity.ok(stackDTOs);
    }

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getPositionList() {
        List<PositionVO> positions = positionService.getPositionList();

        List<PositionDTO> positionList = positions.stream().map(
                entityUtil::convertPositionVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(positionList);
    }

    @GetMapping("/educations")
    public ResponseEntity<List<EducationDTO>> getEducationList() {
        List<EducationVO> educations = educationService.getEducationList();

        List<EducationDTO> educationList = educations.stream().map(
                entityUtil::convertEducationVoToDto
        ).collect(Collectors.toList());
        return ResponseEntity.ok(educationList);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<StudyCategoryDTO>> getCategoryList() {
        List<StudyCategoryVO> categoryList = studyCategoryService.getCategoryList();

        List<StudyCategoryDTO> studyCategoryDTOList = categoryList.stream().map(
                i -> entityUtil.convertStudyCategoryVoToDto(i)
        ).collect(Collectors.toList());

        return ResponseEntity.ok(studyCategoryDTOList);
    }
}
