package com.jakduk.api.restcontroller.vo.admin;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by pyohwan on 16. 6. 7.
 */

public class LeagueAttendanceForm {

    private String id;

    @NotEmpty
    private String competitionId;

    @NotNull
    private Integer season;

    @NotNull
    private Integer games;

    @NotNull
    private Integer total;

    @NotNull
    private Integer average;

    @NotNull
    private Integer numberOfClubs;

    public String getId() {
        return id;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getGames() {
        return games;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getAverage() {
        return average;
    }

    public Integer getNumberOfClubs() {
        return numberOfClubs;
    }
}
