package com.limbae.pfy.util;

import com.limbae.pfy.domain.ImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    public byte[] getImageByteFromImageVO(ImageVO uvo)
            throws IOException {
        File file = new File(originPath + uvo.getUploadPath() +"/"+ uvo.getSaveName());
        byte[] byfile = null;

        //it can throw IOException
        byfile = Files.readAllBytes(file.toPath());

        return byfile;
    }

    public boolean saveImage(MultipartFile multipartFile, String name, String uploadPath) throws IOException {

        File file = new File(originPath + uploadPath + "/" + name);

        //it can throw IOException
        multipartFile.transferTo(file);

        return true;

    }

}
