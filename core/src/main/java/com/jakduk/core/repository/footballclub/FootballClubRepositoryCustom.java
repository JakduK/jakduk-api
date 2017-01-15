package com.jakduk.core.repository.footballclub;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.FootballClub;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 17. 1. 10.
 */
public interface FootballClubRepositoryCustom {

    /**
     * 축구단 목록 정렬해서 가져온다.
     */
    List<FootballClub> findFootballClubs(List<ObjectId> ids, String language, CoreConst.NAME_TYPE sortNameType);
}
