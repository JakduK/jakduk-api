package com.jakduk.core.service;

import com.jakduk.core.common.util.FileUtils;
import com.jakduk.core.repository.gallery.GalleryRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by pyohwanjang on 2017. 4. 18..
 */

@Service
public class CommonGalleryService {

    @Value("${core.storage.image.path}")
    private String storageImagePath;

    @Value("${core.storage.thumbnail.path}")
    private String storageThumbnailPath;

    @Autowired
    private GalleryRepository galleryRepository;

    public void deleteGallery(String id, String contentType) {
        ObjectId objectId = new ObjectId(id);
        LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 사진 포맷.
        String formatName = StringUtils.split(contentType, "/")[1];
        String fileName = id + "." + formatName;

        FileUtils.removeImageFile(storageImagePath, localDate, fileName);
        FileUtils.removeImageFile(storageThumbnailPath, localDate, fileName);

        galleryRepository.delete(id);
    }

}
