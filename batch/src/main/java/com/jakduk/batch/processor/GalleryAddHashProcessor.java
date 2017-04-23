package com.jakduk.batch.processor;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.Gallery;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by pyohwanjang on 2017. 4. 11..
 */
public class GalleryAddHashProcessor implements ItemProcessor<Gallery, Gallery> {

    @Value("${core.storage.image.path}")
    private String storageImagePath;

    @Override
    public Gallery process(Gallery item) throws Exception {

        ObjectId objId = new ObjectId(item.getId());
        Instant instant = Instant.ofEpochMilli(objId.getDate().getTime());
        LocalDateTime timePoint = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        String formatName = StringUtils.split(item.getContentType(), "/")[1];

        Path imagePath = Paths.get(storageImagePath, String.valueOf(timePoint.getYear()), String.valueOf(timePoint.getMonthValue()),
                String.valueOf(timePoint.getDayOfMonth()), item.getId() + "." + formatName);

        if (Files.exists(imagePath, LinkOption.NOFOLLOW_LINKS)) {
            try {

                byte[] bytes = Files.readAllBytes(imagePath);
                String hash = DigestUtils.md5DigestAsHex(bytes);

                item.setHash(hash);
                System.out.println("path=" + imagePath + ", gallery id=" + item.getId());

                List<CoreConst.BATCH_TYPE> batchList = Optional.ofNullable(item.getBatch())
                        .orElseGet(ArrayList::new);

                if (batchList.stream().noneMatch(batch -> batch.equals(CoreConst.BATCH_TYPE.GALLERY_ADD_HASH_FIELD_01))) {
                    batchList.add(CoreConst.BATCH_TYPE.GALLERY_ADD_HASH_FIELD_01);
                    item.setBatch(batchList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not exist path=" + imagePath);
        }

        return item;
    }
}
