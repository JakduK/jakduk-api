package com.jakduk.restcontroller.vo;

import com.jakduk.model.db.Competition;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by pyohwan on 16. 6. 7.
 */

@Data
public class LeagueAttendanceForm {

    private String id;

    private String competitionId;

    private Integer season;

    private Integer games;

    private Integer total;

    private Integer average;

    private Integer numberOfClubs;
}
