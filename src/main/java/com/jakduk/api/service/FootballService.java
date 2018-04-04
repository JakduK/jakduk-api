package com.jakduk.api.service;


import com.jakduk.api.common.Constants;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.FootballClubOrigin;
import com.jakduk.api.repository.footballclub.FootballClubOriginRepository;
import com.jakduk.api.repository.footballclub.FootballClubRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<FootballClub> getFootballClubs(String language, Constants.CLUB_TYPE clubType, Constants.NAME_TYPE sortNameType) {

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
    public List<FootballClub> getFootballClubs(List<ObjectId> ids, String language, Constants.NAME_TYPE sortNameType ) {

        return footballClubRepository.findFootballClubs(ids, language, sortNameType);
    }

    // 대회 목록.
    public List<Competition> getCompetitions(List<ObjectId> ids, String language) {
        return jakdukDAO.getCompetitions(ids, language);
    }
}
