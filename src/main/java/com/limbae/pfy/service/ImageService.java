package com.limbae.pfy.service;


import com.limbae.pfy.domain.UiImageVO;
import com.limbae.pfy.repository.UiImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    UiImageRepository uiImageRepository;

    public Optional<UiImageVO> getUiImageWithName(String name){
        return uiImageRepository.findOneByName(name);
    }




}
