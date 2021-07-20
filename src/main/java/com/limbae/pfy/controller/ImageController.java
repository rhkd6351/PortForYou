package com.limbae.pfy.controller;


import com.limbae.pfy.domain.UiImageVO;
import com.limbae.pfy.domain.UserVO;
import com.limbae.pfy.dto.ResponseObjectDTO;
import com.limbae.pfy.service.ImageService;
import com.limbae.pfy.service.UserService;
import com.limbae.pfy.util.ImageUtil;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/img")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    UserService userService;

    @GetMapping(
            value = "/default",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> getDefaultImageWithName(
            @RequestParam("name") String name) throws IOException {


        Optional<UiImageVO> img = imageService.getUiImageWithName(name);
        byte[] bfile = null;

        if (img.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try {
            bfile = imageUtil.getImageByteFromImageVO(img.get()).get();
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bfile, HttpStatus.OK);
    }

    @PostMapping(
            value = "/user"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseObjectDTO> saveUserProfile(
            @RequestParam("profile") MultipartFile multipartFile) throws IOException {
        Optional<UserVO> user = userService.getMyUserWithAuthorities();

        int i = multipartFile.getOriginalFilename().lastIndexOf(".");
        String extension = multipartFile.getOriginalFilename().substring(i);

        //파일 확장자 제한
        if(!(extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".png") || extension.equals(".git")))
            return new ResponseEntity<ResponseObjectDTO>(
                    ResponseObjectDTO.builder().message("wrong file extension").build(),
                    HttpStatus.BAD_REQUEST
            );

        //파일 용량 제한
        if(multipartFile.getSize() > 7000000)
            return new ResponseEntity<ResponseObjectDTO>(
                    ResponseObjectDTO.builder().message("File size exceeded").build(),
                    HttpStatus.BAD_REQUEST
            );

        String saveName = user.get().getUid() + "_profile_img";

        try {
            imageUtil.saveImage(multipartFile, saveName + extension, "/user/profile");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        Optional<UiImageVO> image = imageService.getUiImageWithName(saveName);
        if(image.isEmpty()){
            imageService.saveUiImage(UiImageVO.builder()
                    .name(saveName)
                    .saveName(saveName + extension)
                    .originalName(multipartFile.getOriginalFilename())
                    .size(multipartFile.getSize())
                    .uploadPath("/user/profile")
                    .build());
        }else{
            UiImageVO _image = image.get();
            _image.setSaveName(saveName + extension);
            _image.setSize(multipartFile.getSize());
            _image.setOriginalName(multipartFile.getOriginalFilename());
            _image.setUploadPath("/user/profile");
            imageService.saveUiImage(_image);
        }

        return ResponseEntity.ok(ResponseObjectDTO.builder().message("success").build());

    }
}
