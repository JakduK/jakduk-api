package com.jakduk.core.repository.footballclub;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.FootballClub;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

/**
 * Created by pyohwan on 17. 1. 10.
 */

@Repository
public class FootballClubRepositoryImpl implements FootballClubRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 축구단 목록 정렬해서 가져온다.
     */
    @Override
    public List<FootballClub> findFootballClubs(List<ObjectId> ids, String language, CoreConst.NAME_TYPE sortNameType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("names.language").is(language));
        query.addCriteria(Criteria.where("origin.$id").in(ids));
        query.fields().include("active").include("origin").include("names.$");
        //query.with(new Sort(Sort.Direction.DESC, "names.fullName"));
        List<FootballClub> footballClubs = mongoTemplate.find(query, FootballClub.class);

        switch (sortNameType) {
            case fullName:
                footballClubs.sort(Comparator.comparing(f -> f.getNames().get(0).getFullName()));
                break;
            case shortName:
                footballClubs.sort(Comparator.comparing(f -> f.getNames().get(0).getShortName()));
                break;
        }

        return footballClubs;
    }
}
