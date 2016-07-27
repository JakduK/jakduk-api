package com.jakduk.service;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.Competition;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.repository.FootballClubRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@Service
public class FootballService {

    @Autowired
    private JakdukDAO jakdukDAO;

    @Autowired
    private FootballClubRepository footballClubRepository;

    @Autowired
    private FootballClubOriginRepository footballClubOriginRepository;

    public FootballClub findById(String id) {
        return footballClubRepository.findOne(id);
    }

    /**
     * 조건에 맞는 축구단 목록을 가져온다.
     * @param language 언어
     * @param clubType 클럽 성격
     * @param sortNameType 정렬 기준
     * @return
     */
    public List<FootballClub> getFootballClubs(String language, CommonConst.CLUB_TYPE clubType, CommonConst.NAME_TYPE sortNameType) {

        List<FootballClubOrigin> fcos = footballClubOriginRepository.findByClubType(clubType);
        List<ObjectId> ids = new ArrayList<ObjectId>();

        for (FootballClubOrigin fco : fcos) {
            String id = fco.getId();
            ids.add(new ObjectId(id));
        }

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubs(ids, language, sortNameType);

        return footballClubs;
    }

    /**
     * 조건에 맞는 축구단 목록을 가져온다.
     * @param language 언어
     * @param sortNameType 정렬 기준
     * @param ids footballClub Id 배열
     * @return 축구단 배열
     */
    public List<FootballClub> getFootballClubs(List<ObjectId> ids, String language, CommonConst.NAME_TYPE sortNameType ) {

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubs(ids, language, sortNameType);

        return footballClubs;
    }

    /**
     * 해당 언어에 맞는 축구단 이름 가져오기.
     * @param footballClub 축구단 객체
     * @param language 언어
     * @return LocalName 객체
     */
    public LocalName getLocalNameOfFootballClub(FootballClub footballClub, String language) {
        LocalName localName = null;

        if (Objects.nonNull(footballClub)) {
            List<LocalName> names = footballClub.getNames();

            for (LocalName name : names) {
                if (name.getLanguage().equals(language)) {
                    localName = name;
                }
            }
        }

        return localName;
    }

    // 대회 목록.
    public List<Competition> getCompetitions(List<ObjectId> ids, String language) {
        return jakdukDAO.getCompetitions(ids, language);
    }
}
