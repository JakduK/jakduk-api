package com.jakduk.service;

import com.jakduk.common.CommonConst;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.embedded.LocalName;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.repository.FootballClubRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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

    public List<FootballClub> getFootballClubs(String language, CommonConst.CLUB_TYPE clubType, CommonConst.NAME_TYPE sortNameType) {

        List<FootballClubOrigin> fcos = footballClubOriginRepository.findByClubType(clubType);
        List<ObjectId> ids = new ArrayList<ObjectId>();

        for (FootballClubOrigin fco : fcos) {
            String id = fco.getId();
            ids.add(new ObjectId(id));
        }

        List<FootballClub> footballClubs = jakdukDAO.getFootballClubList(ids, language, sortNameType);

        return footballClubs;
    }

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
}
