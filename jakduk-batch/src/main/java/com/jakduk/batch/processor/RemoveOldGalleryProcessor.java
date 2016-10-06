package com.jakduk.batch.processor;

import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.repository.GalleryRepository;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by pyohwan on 16. 10. 6.
 */
public class RemoveOldGalleryProcessor implements ItemProcessor<Gallery, Gallery> {

    @Value("${storage.image.path}")
    private String storageImagePath;

    @Value("${storage.thumbnail.path}")
    private String storageThumbnailPath;

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    public Gallery process(Gallery item) {

        ObjectId objId = new ObjectId(item.getId());
        Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
        LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        Path imagePath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
                String.valueOf(timePoint.getDayOfMonth()), item.getId());

        Path thumbPath = Paths.get(storageThumbnailPath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
                String.valueOf(timePoint.getDayOfMonth()), item.getId());

        if (Files.exists(imagePath, LinkOption.NOFOLLOW_LINKS) && Files.exists(thumbPath, LinkOption.NOFOLLOW_LINKS)) {

            deleteGalleryFile(imagePath);
            deleteGalleryFile(thumbPath);

            System.out.println("path=" + imagePath + ", gallery id=" + item.getId());

            galleryRepository.delete(item);
        }

        return item;
    }

    private void deleteGalleryFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Path imagePathOfDay = path.getParent();
            DirectoryStream<Path> imagePathOfDayDs = Files.newDirectoryStream(imagePathOfDay);

            if (! imagePathOfDayDs.iterator().hasNext()) {
                Files.deleteIfExists(imagePathOfDay);

                Path imagePathOfMonth = imagePathOfDay.getParent();
                DirectoryStream<Path> imagePathOfMonthDs = Files.newDirectoryStream(imagePathOfMonth);

                if (! imagePathOfMonthDs.iterator().hasNext()) {
                    Files.deleteIfExists(imagePathOfMonth);

                    Path imagePathOfYear = imagePathOfMonth.getParent();
                    DirectoryStream<Path> imagePathOfYearDs = Files.newDirectoryStream(imagePathOfYear);

                    if (! imagePathOfYearDs.iterator().hasNext())
                        Files.deleteIfExists(imagePathOfYear);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
