package com.limbae.pfy.util;

import com.limbae.pfy.domain.UiImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
            System.out.println("There is no such file");
            log.info("wow");
            throw e;
        }

        return Optional.of(byfile);
    }
}
