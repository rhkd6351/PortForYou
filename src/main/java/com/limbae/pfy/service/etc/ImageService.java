package com.limbae.pfy.service.etc;


import com.limbae.pfy.domain.etc.ImageVO;
import com.limbae.pfy.repository.etc.ImageRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public ImageVO getByName(String name) throws NotFoundException{
        Optional<ImageVO> image = imageRepository.findOneByName(name);

        if(image.isEmpty())
            throw new NotFoundException("invalid image nmae");

        return image.get();
    }

    public void save(ImageVO imageVO){
        imageRepository.saveAndFlush(imageVO);
    }



}
