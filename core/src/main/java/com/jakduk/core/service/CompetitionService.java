package com.jakduk.core.service;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.Competition;
import com.jakduk.core.repository.CompetitionRepository;
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
