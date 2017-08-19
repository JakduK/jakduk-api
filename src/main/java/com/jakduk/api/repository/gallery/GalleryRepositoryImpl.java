package com.jakduk.api.repository.gallery;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.Gallery;
import com.jakduk.api.model.simple.GallerySimple;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 11. 30.
 */

@Repository
public class GalleryRepositoryImpl implements GalleryRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 사진첩 보기의 앞, 뒤 사진을 가져온다.
     */
    @Override
    public List<Gallery> findGalleriesById(ObjectId id, Constants.CRITERIA_OPERATOR operator, Integer limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.status").is(Constants.GALLERY_STATUS_TYPE.ENABLE.name()));
        query.limit(limit);

        if (Objects.nonNull(id)) {
            switch (operator) {
                case GT:
                    query.addCriteria(Criteria.where("_id").gt(id));
                    break;
                case LT:
                    query.addCriteria(Criteria.where("_id").lt(id));
                    break;
            }
        }

        query.with(new Sort(Sort.Direction.DESC, "_id"));

        return mongoTemplate.find(query, Gallery.class);
    }

    /**
     * ItemID와 FromType에 해당하는 Gallery 목록을 가져온다.
     */
    @Override
    public List<Gallery> findByItemIdAndFromType(ObjectId itemId, Constants.GALLERY_FROM_TYPE fromType, Integer limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status.status").is(Constants.GALLERY_STATUS_TYPE.ENABLE.name()));
        query.addCriteria(Criteria.where("linkedItems._id").is(itemId));
        query.addCriteria(Criteria.where("linkedItems.from").is(fromType));
        query.limit(limit);

        query.with(new Sort(Sort.Direction.DESC, "_id"));

        return mongoTemplate.find(query, Gallery.class);
    }

    @Override
    public List<GallerySimple> findSimpleById(ObjectId id, Integer limit) {

        Query query = new Query();
        query.addCriteria(Criteria.where("status.status").is(Constants.GALLERY_STATUS_TYPE.ENABLE.name()));
        query.limit(limit);

        if (Objects.nonNull(id)) {
            query.addCriteria(Criteria.where("_id").lt(id));
        }

        query.with(new Sort(Sort.Direction.DESC, "_id"));

        return mongoTemplate.find(query, GallerySimple.class);
    }
}
