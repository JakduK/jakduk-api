package com.jakduk.core.gallery;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.simple.GalleryOnList;
import com.jakduk.core.repository.gallery.GalleryRepository;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 4. 4..
 */
public class GalleryRepositoryTest extends CoreApplicationTests {

    @Autowired
    private GalleryRepository sut;

    @Test
    public void findGalleriesById() {
        List<GalleryOnList> galleries = sut.findGalleriesById(new ObjectId("58d64035807d714ce35675d4"), CoreConst.CRITERIA_OPERATOR.GT, 3);

        Assert.assertTrue(Objects.nonNull(galleries));
    }
}
