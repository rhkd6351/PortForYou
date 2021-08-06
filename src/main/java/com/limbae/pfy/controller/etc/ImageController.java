package com.limbae.pfy.controller.etc;


import com.limbae.pfy.domain.user.PortfolioVO;
import com.limbae.pfy.domain.etc.ImageVO;
import com.limbae.pfy.domain.user.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.service.etc.ImageService;
import com.limbae.pfy.service.user.PortfolioServiceInterfaceImpl;
import com.limbae.pfy.service.user.UserServiceInterfaceImpl;
import com.limbae.pfy.util.ImageUtil;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;


@RestController
@RequestMapping("/api/img")
public class ImageController {

    ImageService imageService;
    ImageUtil imageUtil;
    UserServiceInterfaceImpl userService;
    PortfolioServiceInterfaceImpl portfolioService;

    @Autowired
    public ImageController(ImageService imageService, ImageUtil imageUtil,
                           UserServiceInterfaceImpl userService, PortfolioServiceInterfaceImpl portfolioService) {
        this.imageService = imageService;
        this.imageUtil = imageUtil;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @GetMapping(value = "/default/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getDefaultImageWithName(
            @PathVariable(value = "name") String name) throws IOException, NotFoundException {

        //it can throw NotFoundException
        ImageVO image = imageService.getByName(name);
        byte[] bfile = null;

        //it can throw IOException
        bfile = imageUtil.getImageByteFromImageVO(image);

        return new ResponseEntity<>(bfile, HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> saveUserProfile(
            @RequestParam("profile") MultipartFile multipartFile) throws IOException, AuthException, NotFoundException, NotSupportedException {
        UserVO user = userService.getByAuth();

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
            image = imageService.getByName(saveName);
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
            imageService.save(image);
        }

        return ResponseEntity.ok(ResponseObjectDTO.builder().message("success").build());

    }

    @PostMapping(value = "/portfolio")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> savePortfolioImg(
            @RequestParam("img") MultipartFile multipartFile,
            @RequestParam("portfolio-idx") Long portfolioIdx) throws AuthException, NotFoundException, NotSupportedException, IOException {
        PortfolioVO portfolioByIdx = portfolioService.getByIdx(portfolioIdx);
        UserVO myUserWithAuthorities = userService.getByAuth();

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
            image = imageService.getByName(saveName);
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
            imageService.save(image);
        }

        return ResponseEntity.ok(ResponseObjectDTO.builder().message("success").build());

    }
}