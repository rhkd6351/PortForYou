package com.limbae.pfy.controller;


import com.limbae.pfy.domain.PortfolioVO;
import com.limbae.pfy.domain.ImageVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.service.ImageService;
import com.limbae.pfy.service.PortfolioService;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.ImageUtil;

import javassist.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/img")
public class ImageController {

    ImageService imageService;
    ImageUtil imageUtil;
    UserService userService;
    PortfolioService portfolioService;

    public ImageController(ImageService imageService, ImageUtil imageUtil,
                           UserService userService, PortfolioService portfolioService) {
        this.imageService = imageService;
        this.imageUtil = imageUtil;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @GetMapping(value = "/default", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getDefaultImageWithName(
            @RequestParam("name") String name) throws IOException, NotFoundException {

        //it can throw NotFoundException
        ImageVO image = imageService.getImageWithName(name);
        byte[] bfile = null;

        //it can throw IOException
        bfile = imageUtil.getImageByteFromImageVO(image);

        return new ResponseEntity<>(bfile, HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> saveUserProfile(
            @RequestParam("profile") MultipartFile multipartFile) throws IOException, AuthException, NotFoundException, NotSupportedException {
        UserVO user = userService.getMyUserWithAuthorities();

        int i = multipartFile.getOriginalFilename().lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(i);

        //파일 확장자 제한
        if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".gif")))
            throw new NotSupportedException("not supported extension : " + extension);

        //파일 용량 제한
        if(multipartFile.getSize() > 7000000)
            throw new NotSupportedException("exceed supported file size");

        String saveName = user.getUid() + "_profile_img";

        //it can throw IOException
        imageUtil.saveImage(multipartFile, saveName + extension, "/user/profile");

        ImageVO image = null;
        try {
            image = imageService.getImageWithName(saveName);
            image.setSaveName(saveName + extension);
            image.setSize(multipartFile.getSize());
            image.setOriginalName(multipartFile.getOriginalFilename());
            image.setUploadPath("/user/profile");
        }catch (NotFoundException e) {
            image = ImageVO.builder()
                    .name(saveName)
                    .saveName(saveName + extension)
                    .originalName(multipartFile.getOriginalFilename())
                    .size(multipartFile.getSize())
                    .uploadPath("/user/profile")
                    .build();
        }finally {
            imageService.saveImage(image);
        }

        return ResponseEntity.ok(ResponseObjectDTO.builder().message("success").build());

    }

    @PostMapping(value = "/portfolio")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> savePortfolioImg(
            @RequestParam("img") MultipartFile multipartFile,
            @RequestParam("portfolio_idx") Long portfolioIdx) throws AuthException, NotFoundException, NotSupportedException, IOException {
        PortfolioVO portfolioByIdx = portfolioService.getPortfolioByIdx(portfolioIdx);
        UserVO myUserWithAuthorities = userService.getMyUserWithAuthorities();

        if((portfolioByIdx.getUser() != myUserWithAuthorities))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


        int i = multipartFile.getOriginalFilename().lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(i);

        //파일 확장자 제한
        if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".gif")))
            throw new NotSupportedException("not supported extension : " + extension);

        //파일 용량 제한
        if(multipartFile.getSize() > 7000000)
            throw new NotSupportedException("exceed supported file size");

        String saveName = portfolioIdx + "_portfolio_img"; // TODO
        //it can throw IOException
        imageUtil.saveImage(multipartFile, saveName + extension, "/user/portfolio");

        ImageVO image = null;
        try {
            image = imageService.getImageWithName(saveName);
            image.setSaveName(saveName + extension);
            image.setSize(multipartFile.getSize());
            image.setOriginalName(multipartFile.getOriginalFilename());
            image.setUploadPath("/user/portfolio");
        }catch (NotFoundException e) {
            image = ImageVO.builder()
                    .name(saveName)
                    .saveName(saveName + extension)
                    .originalName(multipartFile.getOriginalFilename())
                    .size(multipartFile.getSize())
                    .uploadPath("/user/portfolio")
                    .build();
        }finally {
            imageService.saveImage(image);
        }

        return ResponseEntity.ok(ResponseObjectDTO.builder().message("success").build());

    }
}
