package com.jakduk.core.repository.board.category;

import com.jakduk.core.model.db.BoardCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 30.
 */

@Repository
public class BoardCategoryRepositoryImpl implements BoardCategoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 해당 언어에 맞는 게시판 말머리 목록을 가져온다.
     * @param language 언어
     * @return 말머리 배열
     */
    @Override
    public List<BoardCategory> findByLanguage(String language) {
        Query query = new Query();
        query.addCriteria(Criteria.where("names.language").is(language));
        query.fields().include("code").include("names.$");

        return mongoTemplate.find(query, BoardCategory.class);
    }
}
