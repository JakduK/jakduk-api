package com.jakduk.core.repository.gallery;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.elasticsearch.ESGallery;
import com.jakduk.core.model.simple.GalleryOnList;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by pyohwan on 16. 11. 30.
 */
public interface GalleryRepositoryCustom {

    // 기준 Gallery ID 이상의 Gallery 목록을 가져온다.
    List<ESGallery> findGalleriesGreaterThanId(ObjectId objectId, Integer limit);

    /**
     * 사진첩 보기의 앞, 뒤 사진을 가져온다.
     */
    List<GalleryOnList> findGalleriesById(ObjectId id, CoreConst.CRITERIA_OPERATOR operator, Integer limit);
}
