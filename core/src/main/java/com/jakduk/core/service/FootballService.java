package com.jakduk.core.service;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Competition;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.db.FootballClubOrigin;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.repository.footballclub.FootballClubOriginRepository;
import com.jakduk.core.repository.footballclub.FootballClubRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 * 16. 3. 20 오후 11:03
 */

@Service
public class FootballService {

    @Autowired
    private JakdukDAO jakdukDAO;

    @Autowired
    private FootballClubRepository footballClubRepository;

    @Autowired
    private FootballClubOriginRepository footballClubOriginRepository;

    public FootballClub findOneById(String id) {
        return footballClubRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));
    }

    /**
     * 조건에 맞는 축구단 목록을 가져온다.
     *
     * @param language 언어
     * @param clubType 클럽 성격
     * @param sortNameType 정렬 기준
     */
    public List<FootballClub> getFootballClubs(String language, CoreConst.CLUB_TYPE clubType, CoreConst.NAME_TYPE sortNameType) {

        List<FootballClubOrigin> footballClubOrigins = footballClubOriginRepository.findByClubType(clubType);

        List<ObjectId> ids = footballClubOrigins.stream()
                .map(footballClubOrigin -> new ObjectId(footballClubOrigin.getId()))
                .collect(Collectors.toList());

        return footballClubRepository.findFootballClubs(ids, language, sortNameType);
    }

    /**
     * 조건에 맞는 축구단 목록을 가져온다.
     *
     * @param language 언어
     * @param sortNameType 정렬 기준
     * @param ids footballClub Id 배열
     * @return 축구단 배열
     */
    public List<FootballClub> getFootballClubs(List<ObjectId> ids, String language, CoreConst.NAME_TYPE sortNameType ) {

        return footballClubRepository.findFootballClubs(ids, language, sortNameType);
    }

    /**
     * 해당 언어에 맞는 축구단 이름 가져오기.
     *
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
