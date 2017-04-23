package com.jakduk.core.model.jongo;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.MongoId;

/**
 * Created by pyohwanjang on 2017. 4. 22..
 */

@Getter
public class GalleryFeelingCount {

    @MongoId //see NewAnnotationsCompatibilitySuiteTest for more informations
    private ObjectId id;

    private int usersLikingCount;

    private int usersDisLikingCount;

}
