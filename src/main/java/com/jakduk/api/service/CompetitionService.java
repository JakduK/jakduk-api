package com.jakduk.api.service;


import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Competition;
import com.jakduk.api.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pyohwan
 * 16. 6. 6 오후 11:03
 */

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    // 대회 목록.
    public List<Competition> findCompetitions() {
        return competitionRepository.findAll();
    }

    // 대회 하나.
    public Competition findOneById(String id) {
        return competitionRepository.findOneById(id)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_COMPETITION));
    }

    // 대회 하나.
    public Competition findCompetitionByCode(String code) {
        return competitionRepository.findOneByCode(code);
    }
}
