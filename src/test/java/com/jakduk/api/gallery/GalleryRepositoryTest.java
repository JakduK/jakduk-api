package com.jakduk.api.gallery;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.repository.gallery.GalleryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */

@DataMongoTest
public class GalleryRepositoryTest {

    @Autowired
    private GalleryRepository repository;

    @Test
    public void findGalleriesById() {
        List<Gallery> galleries = repository.findGalleriesById(new ObjectId("58d64035807d714ce35675d4"), Constants.CRITERIA_OPERATOR.GT, 3);

        assertTrue(Objects.nonNull(galleries));
    }

    @Test
    public void findByItemIdAndFromType() {
        List<Gallery> galleries = repository.findByItemIdAndFromType(new ObjectId("58ee422be846b60526cd3382"), Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, 10);

        assertTrue(Objects.nonNull(galleries));
    }
    
}
