package com.jakduk.core.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.FileUtils;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.LinkedItem;
import com.jakduk.core.repository.gallery.GalleryRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

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

    @Autowired
    private CommonSearchService commonSearchService;

    /**
     * Gallery와 사진 파일 지움
     *
     * @param id Gallery ID
     * @param contentType contentType
     */
    public void deleteGallery(String id, String contentType, Boolean deleteSearchDocument) {
        ObjectId objectId = new ObjectId(id);
        LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 사진 포맷.
        String formatName = StringUtils.split(contentType, "/")[1];
        String fileName = id + "." + formatName;

        FileUtils.removeImageFile(storageImagePath, localDate, fileName);
        FileUtils.removeImageFile(storageThumbnailPath, localDate, fileName);

        galleryRepository.delete(id);

        // 엘라스틱 서치 document 삭제.
        if (deleteSearchDocument)
            commonSearchService.deleteDocumentGallery(id);
    }

    /**
     * Gallery와 연결된 아이템(BoardFree, BoardFreeComment)을 끊는다
     *
     * @param itemId Item ID
     * @param fromType 출처
     */
    public void unlinkGalleries(String itemId, CoreConst.GALLERY_FROM_TYPE fromType) {
        List<Gallery> galleries = galleryRepository.findByItemIdAndFromType(
                new ObjectId(itemId), fromType, 100);

        galleries.forEach(gallery -> {
            List<LinkedItem> linkedItems = gallery.getLinkedItems();
            Boolean removed = linkedItems.removeIf(
                    linkedItem -> linkedItem.getId().equals(itemId) && linkedItem.getFrom().equals(fromType));

            if (removed && linkedItems.size() >= 1) {
                gallery.setLinkedItems(linkedItems);
                galleryRepository.save(gallery);
            } else if (removed && linkedItems.size() < 1) {
                this.deleteGallery(gallery.getId(), gallery.getContentType(), true);
            }
        });
    }

}
