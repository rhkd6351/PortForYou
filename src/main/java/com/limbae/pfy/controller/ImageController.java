package com.limbae.pfy.controller;


import com.limbae.pfy.domain.UiImageVO;
import com.limbae.pfy.service.ImageService;
import com.limbae.pfy.util.ImageUtil;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/api/img")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageUtil imageUtil;

    @GetMapping(
            value = "/default",
            produces = MediaType.IMAGE_PNG_VALUE
            )
    public ResponseEntity<byte[]> getDefaultImageWithName(
            @RequestParam("name") String name) throws IOException {


        Optional<UiImageVO> img = imageService.getUiImageWithName(name);
        byte[] bfile = null;

        if(img.isEmpty()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        try {
            bfile = imageUtil.getImageByteFromImageVO(img.get()).get();
        }catch (IOException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(bfile, HttpStatus.OK);
    }

}
