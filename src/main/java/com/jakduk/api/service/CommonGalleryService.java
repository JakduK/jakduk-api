package com.jakduk.api.service;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.FileUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.embedded.LinkedItem;
import com.jakduk.api.repository.gallery.GalleryRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Created by pyohwanjang on 2017. 4. 18..
 */

@Service
public class CommonGalleryService {

    @Resource private JakdukProperties.Storage storageProperties;
    @Autowired private GalleryRepository galleryRepository;
    @Autowired private RabbitMQPublisher rabbitMQPublisher;

    /**
     * Gallery와 사진 파일 지움
     *
     * @param id Gallery ID
     * @param contentType contentType
     */
    public void deleteGallery(String id, String contentType) {
        ObjectId objectId = new ObjectId(id);
        LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 사진 포맷.
        String formatName = StringUtils.split(contentType, "/")[1];
        String fileName = id + "." + formatName;

        FileUtils.removeImageFile(storageProperties.getImagePath(), localDate, fileName);
        FileUtils.removeImageFile(storageProperties.getThumbnailPath(), localDate, fileName);

        galleryRepository.deleteById(id);
    }

    /**
     * Gallery와 연결된 아이템(BoardFree, BoardFreeComment)을 끊는다
     *
     * @param itemId Item ID
     * @param fromType 출처
     */
    public void unlinkGalleries(String itemId, JakdukConst.GALLERY_FROM_TYPE fromType) {
        List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(new ObjectId(itemId), fromType, 100);

        galleries.forEach(gallery -> {
            List<LinkedItem> linkedItems = gallery.getLinkedItems();
            Boolean removed = linkedItems.removeIf(
                    linkedItem -> linkedItem.getId().equals(itemId) && linkedItem.getFrom().equals(fromType));

            if (removed && linkedItems.size() >= 1) {
                gallery.setLinkedItems(linkedItems);
                galleryRepository.save(gallery);
            } else if (removed && linkedItems.size() < 1) {
                this.deleteGallery(gallery.getId(), gallery.getContentType());
                rabbitMQPublisher.deleteDocumentGallery(gallery.getId());
            }
        });
    }

}
