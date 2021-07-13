package com.limbae.pfy.util;

import com.limbae.pfy.domain.UiImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

@Component
@Slf4j
public class ImageUtil {

    private final String originPath;

    public ImageUtil(@Value("${img.path}") String originPath) {
        this.originPath = originPath;
    }

    public Optional<byte[]> getImageByteFromImageVO(
            UiImageVO uvo) throws IOException {
        File file = new File(originPath + uvo.getUploadPath() +"/"+ uvo.getSaveName());
        byte[] byfile = null;

        try{
            byfile = Files.readAllBytes(file.toPath());
        }catch (NoSuchFileException e){
            log.warn("There is no such file");
            throw e;
        }

        return Optional.of(byfile);
    }

    public boolean saveImage(MultipartFile multipartFile, String name, String uploadPath) throws IOException {
        try{
            File file = new File(originPath + uploadPath + "/" + name);
            multipartFile.transferTo(file);
        }catch (Exception e){
            throw e;
        }

        return true;

    }

}
