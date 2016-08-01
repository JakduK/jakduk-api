package com.jakduk.api.restcontroller.vo;

import lombok.Data;

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
