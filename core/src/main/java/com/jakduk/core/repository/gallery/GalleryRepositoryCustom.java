package com.jakduk.core.repository.gallery;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.elasticsearch.EsGallery;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface GalleryRepositoryCustom {

    // 기준 Gallery ID 이상의 Gallery 목록을 가져온다.
    List<EsGallery> findGalleriesGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * 사진첩 보기의 앞, 뒤 사진을 가져온다.
     */
    List<Gallery> findGalleriesById(ObjectId id, CoreConst.CRITERIA_OPERATOR operator, Integer limit);

    /**
     * ItemID와 FromType에 해당하는 Gallery 목록을 가져온다.
     */
    List<Gallery> findByItemIdAndFromType(ObjectId itemId, CoreConst.GALLERY_FROM_TYPE fromType, Integer limit);

}
