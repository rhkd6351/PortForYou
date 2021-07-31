package com.limbae.pfy.service;


import com.limbae.pfy.domain.ImageVO;
import com.limbae.pfy.repository.ImageRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public ImageVO getImageWithName(String name) throws NotFoundException{
        Optional<ImageVO> image = imageRepository.findOneByName(name);
        if(image.isEmpty())
            throw new NotFoundException("invalid image nmae");

        return image.get();
    }

    public void saveImage(ImageVO imageVO){
        imageRepository.saveAndFlush(imageVO);
    }



}
