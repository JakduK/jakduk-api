package com.jakduk.service;

import com.jakduk.model.db.Competition;
import com.jakduk.repository.CompetitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pyohwan on 16. 6. 6.
 */

@Slf4j
@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    // 대회 목록.
    public List<Competition> findCompetitions() {
        return competitionRepository.findAll();
    }

    // 대회 하나.
    public Competition findCompetitionById(String id) {
        return competitionRepository.findOne(id);
    }

    // 대회 하나.
    public Competition findCompetitionByCode(String code) {
        return competitionRepository.findOneByCode(code);
    }
}
