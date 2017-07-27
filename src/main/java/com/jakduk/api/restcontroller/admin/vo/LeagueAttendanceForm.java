package com.jakduk.api.restcontroller.admin.vo;

import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by pyohwan on 16. 6. 7.
 */

@Getter
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
}
