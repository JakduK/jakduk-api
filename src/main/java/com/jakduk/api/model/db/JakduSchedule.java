package com.jakduk.api.model.db;

import com.jakduk.api.model.embedded.JakduScheduleScore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author pyohwan
 * 15. 12. 23 오후 11:07
 */

@Document
public class JakduSchedule {

    @Id
    private String id;
    @DBRef
    private JakduScheduleGroup group;
    private Date date;
    @DBRef
    private FootballClubOrigin home;
    @DBRef
    private FootballClubOrigin away;
    @DBRef
    private Competition competition;
    private JakduScheduleScore score;
    private boolean timeUp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JakduScheduleGroup getGroup() {
        return group;
    }

    public void setGroup(JakduScheduleGroup group) {
        this.group = group;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FootballClubOrigin getHome() {
        return home;
    }

    public void setHome(FootballClubOrigin home) {
        this.home = home;
    }

    public FootballClubOrigin getAway() {
        return away;
    }

    public void setAway(FootballClubOrigin away) {
        this.away = away;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public JakduScheduleScore getScore() {
        return score;
    }

    public void setScore(JakduScheduleScore score) {
        this.score = score;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public void setTimeUp(boolean timeUp) {
        this.timeUp = timeUp;
    }
}
