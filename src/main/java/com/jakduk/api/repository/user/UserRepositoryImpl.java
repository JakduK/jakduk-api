package com.jakduk.api.repository.user;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.simple.UserSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<UserSimple> findSimpleUsers() {
        Query query = new Query();
        query.with(Constants.SORT_BY_ID_DESC);
        query.limit(Constants.HOME_SIZE_LINE_NUMBER);

        return mongoTemplate.find(query, UserSimple.class);
    }

}
