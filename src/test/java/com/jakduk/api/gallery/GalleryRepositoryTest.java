package com.jakduk.api.gallery;

import com.jakduk.api.ApiApplicationTests;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.repository.gallery.GalleryRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */
public class GalleryRepositoryTest extends ApiApplicationTests {

    @Autowired
    private GalleryRepository sut;

    @Test
    public void findGalleriesById() {
        List<Gallery> galleries = sut.findGalleriesById(new ObjectId("58d64035807d714ce35675d4"), Constants.CRITERIA_OPERATOR.GT, 3);

        Assert.assertTrue(Objects.nonNull(galleries));
    }

    @Test
    public void findByItemIdAndFromType() {
        List<Gallery> galleries = sut.findByItemIdAndFromType(new ObjectId("58ee422be846b60526cd3382"), Constants.GALLERY_FROM_TYPE.ARTICLE_COMMENT, 10);

        Assert.assertTrue(Objects.nonNull(galleries));
    }
    
}
