package com.limbae.pfy.service.etc;

import com.limbae.pfy.domain.etc.ProjectVO;
import com.limbae.pfy.domain.etc.StackVO;
import com.limbae.pfy.dto.ProjectDTO;
import com.limbae.pfy.repository.ProjectRepository;
import com.limbae.pfy.service.user.PortfolioServiceInterface;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    StackService stackService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, StackService stackService) {
        this.projectRepository = projectRepository;
        this.stackService = stackService;
    }

    public ProjectVO getByIdx(Long idx) throws NotFoundException {

        Optional<ProjectVO> project = projectRepository.findByIdx(idx);

        if(project.isEmpty())
            throw new NotFoundException("invalid project idx");

        return project.get();
    }

    public ProjectVO update(ProjectDTO dto) throws NotFoundException {

        List<StackVO> stacks = dto.getStack().stream().map(
                i -> {
                    try {
                        return stackService.getByIdx(i.getIdx());
                    } catch(NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList());

        ProjectVO project = null;

        if(dto.getIdx() != null) {
            project = this.getByIdx(dto.getIdx());
            project.setTitle(dto.getTitle());
            project.setContent(dto.getContent());
            project.setSite(dto.getSite());
            project.setStack(stacks);
        }else{
            project = ProjectVO.builder()
                    .idx(dto.getIdx())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .site(dto.getSite())
                    .stack(stacks)
                    .build();
        }

        projectRepository.save(project);

        return project;
    }
}
