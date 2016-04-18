package com.jakduk.service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.embedded.LocalName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 3. 20.
 */

@Service
public class FootballService {

    @Autowired
    private CommonService commonService;

    public List<FootballClub> getFootballClubs(String language) {

        List<FootballClub> footballClubs = commonService.getFootballClubs(language, CommonConst.CLUB_TYPE.FOOTBALL_CLUB, CommonConst.NAME_TYPE.fullName);

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
